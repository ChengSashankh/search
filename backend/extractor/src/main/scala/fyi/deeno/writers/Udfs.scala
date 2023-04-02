package fyi.deeno.writers

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions.{concat, concat_ws, lit}

object Udfs {

  def stringify(c: Column) = concat(lit("["), concat_ws(",", c), lit("]"))

}
