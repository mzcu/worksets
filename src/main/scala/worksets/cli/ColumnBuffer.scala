package worksets.cli

import scala.collection.mutable.ListBuffer

@SuppressWarnings(Array("org.wartremover.warts.All"))
class ColumnBuffer {

  private val buf: ListBuffer[ListBuffer[Char]] = ListBuffer()

  def appendColumn(block: String): Unit = {
    var maxLen = Int.MinValue
    block.linesWithSeparators.map(_.stripLineEnd).zipWithIndex.foreach { case (line, i) =>
      if (buf.size == i) buf.append(ListBuffer())
      buf(i).appendAll(line)
      val currentLineLength = buf(i).size
      maxLen = if (currentLineLength > maxLen) currentLineLength else maxLen
    }
    buf.mapInPlace(_.padToInPlace(maxLen + 10, ' ')).mapInPlace(_.append('\t'))
  }

  def format: String = asSeq.mkString("\n")
  def asSeq: Seq[String] = buf.map(_.mkString("")).toList

}