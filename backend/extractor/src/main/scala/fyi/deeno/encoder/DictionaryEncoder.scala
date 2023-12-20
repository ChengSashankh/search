package fyi.deeno.encoder

import fyi.deeno.protocols.data.{Document, EncodedDocument, PositionalIndex, Vocabulary}
import fyi.deeno.writers.TsvDatasetWriter.store
import fyi.deeno.writers.Udfs.stringify
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{array_distinct, col, collect_list, concat_ws, explode, lower, max, monotonically_increasing_id, posexplode, regexp_replace, split, transform}
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
  def encode(vocab: Dataset[Vocabulary], documents: Dataset[Document]): Dataset[PositionalIndex] = {
    import spark.implicits._

    val documentWordPositions = documents
      .select(col("id").as("docId"), col("title").as("docTitle"), posexplode(split(col("text"), "\\s+")))
      .withColumn("col", regexp_replace(lower(col("col")), "[^a-zA-Z, ]+", ""))
      .join(vocab, vocab("word").equalTo(col("col")), "inner")
      .select("docId", "docTitle", "id", "pos")

    val positionalIndex: Dataset[PositionalIndex] = documentWordPositions
      .groupBy("docId", "id", "docTitle").agg(collect_list("pos").as("indexes"))
      .groupBy("id").agg(collect_list("docId").as("docIds"), collect_list("indexes").as("indexes"), collect_list("docTitle").as("docTitles"))
      .withColumn("id", col("id").cast("long"))
      .as[PositionalIndex]

    positionalIndex.show(2, truncate = false)

//    val groupingWindow = Window.partitionBy("docId").orderBy("pos")
//
//    val encodedDocuments = documentWordPositions
//      .withColumn("sortedTerms", collect_list("id").over(groupingWindow))
//      .groupBy("docId").agg(max("sortedTerms").as("encodedWords"))
//      .select(col("docId").as("id"), stringify(col("encodedWords")).as("text"))
//      .as[EncodedDocument]

    positionalIndex
  }

  def execute(documents: Dataset[Document], outputPath: String): Unit = {

    val vocabPath: String = s"$outputPath/vocab"
    val positionalIdxLoc: String = s"$outputPath/positionalIdx"
    val encodedDocumentsLoc: String = s"$outputPath/encodedDocs"

    val vocab: Dataset[Vocabulary] = buildVocabulary(documents)
    val positionalIndex = encode(vocab, documents)

    store(vocab, vocabPath, true)
//    store(encodedDocuments, encodedDocumentsLoc, true)
    store(positionalIndex, positionalIdxLoc, true)

//    encodedDocuments
  }
}
