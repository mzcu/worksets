package worksets.cli

import scala.collection.mutable.ListBuffer

@SuppressWarnings(Array("org.wartremover.warts.All"))
class TextBuffer {

  private val buf: ListBuffer[ListBuffer[Char]] = ListBuffer()
  private var mode: Mode = Row

  def appendColumn(block: String): Unit = {
    val blockStart = mode match {
      case Col(n) => n
      case _ => -1
    }
    require(blockStart >= 0, "Must enter column mode before using appendColumn")
    var maxLen = Int.MinValue
    block.linesWithSeparators.map(_.stripLineEnd).zipWithIndex.foreach { case (line, i) =>
      val blockIndex = blockStart + i
      if (buf.size == blockIndex) buf.append(ListBuffer())
      buf(blockIndex).appendAll(line)
      val currentLineLength = buf(blockIndex).size
      maxLen = if (currentLineLength > maxLen) currentLineLength else maxLen
    }
    buf.slice(blockStart, buf.size).mapInPlace(_.padToInPlace(maxLen + 10, ' ')).mapInPlace(_.append('\t'))
  }

  def appendRow(row: String): Unit = {
    require(mode == Row, "Must enter row mode before using appendRow")
    buf.append(ListBuffer(row: _*))
  }

  def rowMode(): Unit = mode = Row
  def colMode(): Unit = mode = Col(buf.size)
  def format: String = asSeq.mkString("\n")
  def asSeq: Seq[String] = buf.map(_.mkString("")).toList


  sealed trait Mode
  object Row extends Mode
  case class Col(fromIndex: Int) extends Mode

}