package fyi.deeno

import fyi.deeno.encoder.DictionaryEncoder
import fyi.deeno.indexers.SimpleInvertedIndexer
import fyi.deeno.protocols.data.{Document, EncodedDocument, InvertedPosting}
import fyi.deeno.protocols.model.ExtractorConfiguration
import fyi.deeno.readers.{ConfigurationReader, XMLDatasetReader}
import fyi.deeno.writers.TsvDatasetWriter.store
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

import scala.util.{Failure, Success, Try}

object Runner {

  private def getSparkSession(): SparkSession = {
    SparkSession
      .builder
      .appName("extractor")
      .master("local[*]")
      .getOrCreate
  }

  private def readSourceData(sparkSession: SparkSession, sourceDataPath: String): Dataset[Document] = {
    new XMLDatasetReader(sparkSession).read(sourceDataPath).repartition(16)
  }

  def main(args: Array[String]): Unit = {
    implicit val jsonDecoder: Decoder[ExtractorConfiguration] = deriveDecoder[ExtractorConfiguration]

    val spark: SparkSession = getSparkSession()
    val config: ExtractorConfiguration = new ConfigurationReader[ExtractorConfiguration]().read(args(0))
    val dataset: Dataset[Document] = readSourceData(spark, config.sourceDataPath)
//    val index: Dataset[InvertedPosting] = new SimpleInvertedIndexer(spark).index(dataset)
//    val postingsPath: Option[String] = store[InvertedPosting](index, s"${config.outputBasePath}/invertedIndex", true)
    val docs: Dataset[EncodedDocument] = new DictionaryEncoder(spark).execute(dataset, config.outputBasePath)
  }

}
