package com.datastax.spark.connector

import scala.language.implicitConversions

import com.datastax.spark.connector.mapper.NamedColumnRef

sealed trait ColumnSelector
case object AllColumns extends ColumnSelector
case class SomeColumns(@transient _columns: NamedColumnRef*) extends ColumnSelector {
  val columns = _columns.asInstanceOf[Seq[AnyRef]].map {
    case ref: NamedColumnRef ⇒ ref
    case str ⇒ str.toString: NamedColumnRef
  }
}

object SomeColumns {
  @deprecated("Use com.datastax.spark.connector.rdd.SomeColumns instead of Seq", "1.0")
  implicit def seqToSomeColumns(columns: Seq[String]): SomeColumns =
    SomeColumns(columns.map(x => x: NamedColumnRef): _*)

  def unapply(sc: SomeColumns): Option[Seq[NamedColumnRef]] = {
    Some(sc.columns)
  }
}


