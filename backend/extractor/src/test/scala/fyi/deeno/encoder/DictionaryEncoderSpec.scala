package fyi.deeno.encoder

import fyi.deeno.protocols.data.{Document, Vocabulary}
import org.apache.spark.sql.{Dataset, SparkSession}

class DictionaryEncoderSpec extends MainSpec {

  val sparkSession = SparkSession
    .builder().master("local[*]").appName("scala_test").getOrCreate()

  import sparkSession.implicits._

  val sampleDocument: Document = Document(1L, "document title", "this is the text of the document")

  val documents: Dataset[Document] = sparkSession.sparkContext.parallelize(Seq(
    sampleDocument
  )).toDF().as[Document]


  val words: Seq[Vocabulary] = Seq(Vocabulary(25769803776L, "this"),
    Vocabulary(25769803777L, "is"),
    Vocabulary(25769803778L, "the"),
    Vocabulary(25769803779L, "text"),
    Vocabulary(25769803780L, "of"),
    Vocabulary(25769803778L, "the"),
    Vocabulary(25769803781L, "document"))

  val encodedWords = words.map(_.id).mkString(",")

  val expectedVocabulary: Dataset[Vocabulary] = sparkSession.sparkContext.parallelize(words.distinct).toDF().as[Vocabulary]


  "Dictionary encoder" should "build the vocabulary" in {
    val dictionaryEncoder = new DictionaryEncoder(sparkSession)
    val vocabulary: Dataset[Vocabulary] = dictionaryEncoder.buildVocabulary(documents)
    val vocabularyCreated = vocabulary.collect()

    assert(vocabularyCreated.length == 6)
    assert(sampleDocument.text.split("\\s+").forall(word => vocabularyCreated.exists(_.word.equals(word))))
  }

  "Dictionary encoder" should "encode a document" in {
    val dictionaryEncoder = new DictionaryEncoder(sparkSession)
    val (encodedDocuments, positionalIndex) = dictionaryEncoder.encode(expectedVocabulary, documents)

    val encodedDocumentsCreated = encodedDocuments.collect()

    assert(encodedDocumentsCreated.length == 1)
    assert(encodedDocumentsCreated(0).text.mkString(",") == encodedWords)
  }

}
