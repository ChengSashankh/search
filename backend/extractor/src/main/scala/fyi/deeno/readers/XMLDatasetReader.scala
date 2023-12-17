package fyi.deeno.readers

import fyi.deeno.protocols.data.Document
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, SparkSession}

class XMLDatasetReader(spark: SparkSession) {

  def read(sourceFilePath: String, rowTag: String = "page"): Dataset[Document] = {
    import spark.implicits._

    spark.read.format("com.databricks.spark.xml")
      .option("rowTag", rowTag)
      .load(sourceFilePath)
      .select(col("id"), col("title"), col("revision.text._VALUE").alias("text"))
      .limit(16)
      .as[Document]
  }

}
