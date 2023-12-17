package fyi.deeno

import fyi.deeno.encoder.DictionaryEncoder
import fyi.deeno.indexers.SimpleInvertedIndexer
import fyi.deeno.protocols.data.{Document, DocumentMetadata, EncodedDocument, InvertedPosting}
import fyi.deeno.protocols.model.ExtractorConfiguration
import fyi.deeno.readers.{ConfigurationReader, XMLDatasetReader}
import fyi.deeno.writers.TsvDatasetWriter.store
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

import scala.util.{Failure, Success, Try}

object Runner {

  private def getSparkSession(config: ExtractorConfiguration): SparkSession = {
    SparkSession
      .builder
      .appName("extractor")
      .master("local[*]")
      .config("spark.redis.host", config.redisHost)
      .config("spark.redis.port", config.redisPort)
      .config("spark.redis.passwd", config.redisPasswd)
      .getOrCreate
  }

  private def readSourceData(sparkSession: SparkSession, sourceDataPath: String): Dataset[Document] = {
    new XMLDatasetReader(sparkSession).read(sourceDataPath).repartition(16)
  }

  private def extractDocumentMetadata(sparkSession: SparkSession, documents: Dataset[Document]): Dataset[DocumentMetadata] = {
    import sparkSession.implicits._

    documents
      .select(col("id").cast("long"), col("title"))
      .as[DocumentMetadata]
  }

  def main(args: Array[String]): Unit = {
    implicit val jsonDecoder: Decoder[ExtractorConfiguration] = deriveDecoder[ExtractorConfiguration]

    val config: ExtractorConfiguration = new ConfigurationReader[ExtractorConfiguration]().read(args(0))
    val spark: SparkSession = getSparkSession(config)
    val dataset: Dataset[Document] = readSourceData(spark, config.sourceDataPath)
    val docMetadata: Dataset[DocumentMetadata] = extractDocumentMetadata(spark, dataset)
    store[DocumentMetadata](docMetadata, s"${config.outputBasePath}/docs", true)
    val index: Dataset[InvertedPosting] = new SimpleInvertedIndexer(spark).index(dataset)
    store[InvertedPosting](index, s"${config.outputBasePath}/invertedIndex", true)
    new DictionaryEncoder(spark).execute(dataset, config.outputBasePath)
  }

}
