package fyi.deeno.readers

import io.circe.{Decoder, jawn, Error}
import org.apache.spark.sql.SparkSession

import scala.io.Source

class ConfigurationReader[T] {

  def read(filePath: String)(implicit converter: Decoder[T]): T = {
    val bufferedSource = Source.fromFile(filePath)
    jawn.decode[T](bufferedSource.mkString) match {
      case Left(error: Error) => throw new Exception(s"Unable to parse object at path: $filePath")
      case Right(parsedObject: T) => parsedObject
    }
  }

  def read(filePath: String, spark: SparkSession)(implicit converter: Decoder[T]): T = {
    val jsonString: String = spark.sparkContext.wholeTextFiles(filePath).collect().map(_._1).head
    jawn.decode[T](jsonString) match {
      case Left(error: Error) => throw new Exception(s"Unable to parse object at path: $filePath")
      case Right(parsedObject: T) => parsedObject
    }
  }
}
