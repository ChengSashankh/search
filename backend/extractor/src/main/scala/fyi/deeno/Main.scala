package fyi.deeno

import fyi.deeno.indexers.SimpleInvertedIndexer
import fyi.deeno.readers.XMLDatasetReader
import org.apache.spark.sql.{SaveMode, SparkSession}

object Main {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("extractor")
      .master("local[*]")
      .getOrCreate

    val df = new XMLDatasetReader(spark)
      .read("/Users/cksash/Downloads/wikipedia/Wikipedia-20230319144301.xml")

    df.printSchema()
    df.show(1)

    val index = new SimpleInvertedIndexer(spark).index(df)

    index.write
      .option("delimiter", "\t")
      .mode(SaveMode.Overwrite)
      .csv("/Users/cksash/IdeaProjects/looking-glass/search/backend/extractor/src/main/resources/outputIndex")

    index.show(20, false)
  }

}
