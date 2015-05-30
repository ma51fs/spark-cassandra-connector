package com.datastax.spark.connector

import scala.language.implicitConversions

import com.datastax.spark.connector.mapper.IndexedByNameColumnRef

sealed trait ColumnSelector
case object AllColumns extends ColumnSelector
case class SomeColumns(@transient _columns: IndexedByNameColumnRef*) extends ColumnSelector {
  val columns = _columns.asInstanceOf[Seq[AnyRef]].map {
    case ref: IndexedByNameColumnRef ⇒ ref
    case str ⇒ str.toString: IndexedByNameColumnRef
  }
}

object SomeColumns {
  @deprecated("Use com.datastax.spark.connector.rdd.SomeColumns instead of Seq", "1.0")
  implicit def seqToSomeColumns(columns: Seq[String]): SomeColumns =
    SomeColumns(columns.map(x => x: IndexedByNameColumnRef): _*)

  def unapply(sc: SomeColumns): Option[Seq[IndexedByNameColumnRef]] = {
    Some(sc.columns)
  }
}


