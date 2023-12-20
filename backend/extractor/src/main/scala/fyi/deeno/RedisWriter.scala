package fyi.deeno

import fyi.deeno.writers.Udfs.stringify
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{SaveMode, SparkSession}

object RedisWriter {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("extractor")
      .master("local[*]")
      .config("spark.redis.host", "127.0.0.1")
      .config("spark.redis.port", 6379)
      .getOrCreate

    val docs = spark.read
      .parquet("/Users/cksash/Documents/proj/search/backend/extractor/src/main/resources/outputIndex/docs")

    val posIdx = spark.read.parquet("/Users/cksash/IdeaProjects/search/backend/extractor/src/main/resources/outputIndex/positionalIdx")

    val invIdx = spark.read.parquet("/Users/cksash/IdeaProjects/search/backend/extractor/src/main/resources/outputIndex/invertedIndex")

    val vocab = spark.read.parquet("/Users/cksash/IdeaProjects/search/backend/extractor/src/main/resources/outputIndex/vocab")

    docs.write
      .format("org.apache.spark.sql.redis")
      .option("table", "docs")
      .option("key.column", "id")
      .mode(SaveMode.Overwrite)
      .save()

    vocab.write
      .format("org.apache.spark.sql.redis")
      .option("table", "vocab")
      .option("key.column", "id")
      .mode(SaveMode.Overwrite)
      .save()

    vocab.write
      .format("org.apache.spark.sql.redis")
      .option("table", "word2id")
      .option("key.column", "word")
      .mode(SaveMode.Overwrite)
      .save()

    posIdx
      .withColumn("docIds", stringify(col("docIds")))
      .withColumn("docTitles", stringify(col("docTitles")))
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", "posIdx")
      .option("key.column", "id")
      .mode(SaveMode.Overwrite)
      .save()

    invIdx.write
      .format("org.apache.spark.sql.redis")
      .option("table", "invIdx")
      .option("key.column", "word")
      .mode(SaveMode.Overwrite)
      .save()
  }

}
