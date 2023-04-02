package fyi.deeno

import org.apache.spark.sql.SparkSession

import scala.io.{Source, StdIn}

object ParquetInspector {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("extractor")
      .master("local[*]")
      .getOrCreate

    spark.read.parquet("/Users/cksash/IdeaProjects/looking-glass/search/backend/extractor/src/main/resources/outputIndex/positionalIdx")
      .createOrReplaceTempView("posIdx")

    spark.read.parquet("/Users/cksash/IdeaProjects/looking-glass/search/backend/extractor/src/main/resources/outputIndex/invertedIndex")
      .createOrReplaceTempView("invIdx")

    spark.read.parquet("/Users/cksash/IdeaProjects/looking-glass/search/backend/extractor/src/main/resources/outputIndex/vocab")
      .createOrReplaceTempView("vocab")

    spark.sparkContext.setLogLevel("ERROR")

    while (true) {
      println("Please enter your next query")
      val userInput = StdIn.readLine()
      val outputDf = spark.sql(userInput)
      spark.time(outputDf.show(20, false))
    }
  }

}
