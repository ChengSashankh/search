package fyi.deeno.writers

import org.apache.spark.sql.{Dataset, SaveMode}

import scala.util.{Failure, Success, Try}

object RedisDatasetWriter {

  def store[T](dataset: Dataset[T], table: String, keyColumn: String, overwrite: Boolean = false): Option[String] = {
    val saveMode = if (overwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    Try(dataset.write
      .format("org.apache.spark.sql.redis")
      .option("table", table)
      .option("key.column", keyColumn)
      .mode(saveMode)
      .save()) match {
      case Success(value) =>
        println(s"Successfully wrote dataset to $table")
        Some(table)

      case Failure(exception) =>
        println(s"Writing dataset failed with exception: $exception")
        None
    }
  }
}
