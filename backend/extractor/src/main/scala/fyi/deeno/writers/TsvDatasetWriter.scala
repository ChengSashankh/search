package fyi.deeno.writers

import org.apache.spark.sql.{Dataset, SaveMode}

import scala.util.{Failure, Success, Try}

object TsvDatasetWriter {

  def store[T](vocabulary: Dataset[T], path: String, overwrite: Boolean = false): Option[String] = {
    val saveMode = if (overwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    Try(vocabulary.write.bucketBy(16, "id").mode(saveMode).parquet(path)) match {
      case Success(value) =>
        println(s"Successfully wrote dataset to $path")
        Some(path)

      case Failure(exception) =>
        println(s"Writing dataset failed with exception: $exception")
        None
    }
  }

}
