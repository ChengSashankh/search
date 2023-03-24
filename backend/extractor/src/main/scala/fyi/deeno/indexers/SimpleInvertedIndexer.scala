package fyi.deeno.indexers

import fyi.deeno.data.{InvertedPosting, Page, Posting}
import org.apache.spark.sql.{Column, Dataset, SparkSession}
import org.apache.spark.sql.functions.{col, collect_list, collect_set, concat, concat_ws, lit}

class SimpleInvertedIndexer(spark: SparkSession) {
  def stringify(c: Column) = concat(lit("["), concat_ws(",", c), lit("]"))

  def index(pageDf: Dataset[Page]): Dataset[InvertedPosting] = {
    import spark.implicits._

    pageDf
      .flatMap(page => page.text.split(" ")
              .map(word => word.trim.replaceAll("[^A-Za-z]+", ""))
              .map(_.toLowerCase).filter(_.nonEmpty).filter(_.length < 28)
              .map(word => Posting(word, page.id, 1))
      )
      .groupBy(col("word")).agg((collect_set("id")).alias("docs"))
      .withColumn("docs", stringify($"docs"))
      .as[InvertedPosting]
  }

}
