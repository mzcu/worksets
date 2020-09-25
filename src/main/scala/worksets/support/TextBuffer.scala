package worksets.support

import worksets.support.TextBuffer._

import scala.collection.mutable.ListBuffer

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements", "org.wartremover.warts.Var"))
class TextBuffer:

  private val buf: ListBuffer[ListBuffer[Char]] = ListBuffer()
  private var mode: Mode = Row

  def appendColumn(block: String): Unit =
    val blockStart = mode match
      case Col(n) => n
      case _ => -1
    require(blockStart >= 0, "Must enter column mode before using appendColumn")
    var maxLen = Int.MinValue
    block.linesWithSeparators.map(_.stripLineEnd).zipWithIndex.foreach { case (line, i) =>
      val blockIndex = blockStart + i
      if buf.size == blockIndex then buf.append(ListBuffer())
      buf(blockIndex).appendAll(line)
      val currentLineLength = buf(blockIndex).size
      maxLen = if currentLineLength > maxLen then currentLineLength else maxLen
    }
    buf.slice(blockStart, buf.size).mapInPlace(_.padToInPlace(maxLen + 10, ' ')).mapInPlace(_.append('\t'))

  def appendRow(row: String): Unit =
    require(mode == Row, "Must enter row mode before using appendRow")
    buf.append(ListBuffer(row: _*))

  def rowMode(): Unit = mode = Row
  def colMode(): Unit = mode = Col(buf.size)
  def format: String = asSeq.mkString("\n")
  def asSeq: Seq[String] = buf.map(_.mkString("")).toList


  def +(other: TextBuffer): TextBuffer =
    val tb = new TextBuffer
    tb.buf.appendAll(this.buf)
    tb.buf.appendAll(other.buf)
    tb.mode = other.mode
    tb

object TextBuffer {
  sealed trait Mode
  object Row extends Mode
  case class Col(fromIndex: Int) extends Mode
}