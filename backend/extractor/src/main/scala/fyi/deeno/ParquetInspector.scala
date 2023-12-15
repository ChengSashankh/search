package fyi.deeno

import org.apache.spark.sql.SparkSession

import scala.io.{Source, StdIn}
import scala.util.{Try, Success, Failure}

object ParquetInspector {

  def executeQuery(spark: SparkSession, query: String): Unit = {
    val outputDf = spark.sql(query)
    spark.time(outputDf.show(20, false))
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("extractor")
      .master("local[*]")
      .getOrCreate

    spark.read.parquet("/Users/cksash/IdeaProjects/search/backend/extractor/src/main/resources/outputIndex/positionalIdx")
      .createOrReplaceTempView("posIdx")

    spark.read.parquet("/Users/cksash/IdeaProjects/search/backend/extractor/src/main/resources/outputIndex/invertedIndex")
      .createOrReplaceTempView("invIdx")

    spark.read.parquet("/Users/cksash/IdeaProjects/search/backend/extractor/src/main/resources/outputIndex/vocab")
      .createOrReplaceTempView("vocab")

    spark.sparkContext.setLogLevel("ERROR")

    while (true) {
      println("Please enter your next query")
      val userInput = StdIn.readLine()
      Try(executeQuery(spark, userInput)) match {
        case Success(_) => println("Success")
        case Failure(exception) => println(s"Encountered exception $exception")
      }
    }
  }

}
