package fyi.deeno.encoder

import fyi.deeno.protocols.data.{Document, EncodedDocument, NewPositionalIndex, PositionalIndex, Vocabulary}
import fyi.deeno.writers.TsvDatasetWriter.store
import fyi.deeno.writers.Udfs.stringify
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{array_distinct, col, collect_list, concat_ws, explode, max, monotonically_increasing_id, posexplode, split}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class DictionaryEncoder(spark: SparkSession) {

  def buildVocabulary(documents: Dataset[Document]): Dataset[Vocabulary] = {
    import spark.implicits._

    documents
      .select(col("text"))
      .withColumn("words", split(col("text"), "\\s+"))
      .select(explode(array_distinct(col("words"))) as "word")
      .withColumn("id", monotonically_increasing_id())
      .as[Vocabulary]
  }

  def encode(vocab: Dataset[Vocabulary], wordAliases: List[String]): List[Long] = {
    vocab
      .where(col("word").isin(wordAliases: _*))
      .limit(wordAliases.length)
      .collect()
      .toList
      .map(_.id)
  }
  def encode(vocab: Dataset[Vocabulary], documents: Dataset[Document]): (Dataset[EncodedDocument], Dataset[NewPositionalIndex]) = {
    import spark.implicits._

    val documentWordPositions = documents
      .select(col("id").as("docId"), col("title").as("docTitle"), posexplode(split(col("text"), "\\s+")))
      .join(vocab, vocab("word").equalTo(col("col")), "inner")
      .select("docId", "id", "pos")

    val positionalIndex: Dataset[NewPositionalIndex] = documentWordPositions
      .groupBy("docId", "id").agg(collect_list("pos").as("indexes"))
      .withColumn("id", col("id").cast("long"))
      .as[NewPositionalIndex]

    positionalIndex.show(2, false)

    val groupingWindow = Window.partitionBy("docId").orderBy("pos")

    val encodedDocuments = documentWordPositions
      .withColumn("sortedTerms", collect_list("id").over(groupingWindow))
      .groupBy("docId").agg(max("sortedTerms").as("encodedWords"))
      .select(col("docId").as("id"), stringify(col("encodedWords")).as("text"))
      .as[EncodedDocument]

    (encodedDocuments, positionalIndex)
  }

  def execute(documents: Dataset[Document], outputPath: String): Dataset[EncodedDocument] = {

    val vocabPath: String = s"$outputPath/vocab"
    val positionalIdxLoc: String = s"$outputPath/positionalIdx"
    val encodedDocumentsLoc: String = s"$outputPath/encodedDocs"

    val vocab: Dataset[Vocabulary] = buildVocabulary(documents)
    val (encodedDocuments, positionalIndex) = encode(vocab, documents)

    store(vocab, vocabPath, true)
//    store(encodedDocuments, encodedDocumentsLoc, true)
    store(positionalIndex, positionalIdxLoc, true)

    encodedDocuments
  }
}
