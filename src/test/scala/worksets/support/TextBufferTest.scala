package worksets.support

import worksets.UnitSpec

/**
 * Created by on 02-08-20.
 */
class TextBufferTest extends UnitSpec:

  private val colSeparator = " "*10

  it should "append columns" in {
    val tb = new TextBuffer
    tb.colMode()
    tb appendColumn
      """|a
         |b
         |c
         |""".stripMargin
    tb appendColumn
      """|a1
         |b1
         |c1
         |""".stripMargin
    tb.format.linesWithSeparators.toList should
      contain theSameElementsInOrderAs List(
      s"a${colSeparator}a1$colSeparator\n",
      s"b${colSeparator}b1$colSeparator\n",
      s"c${colSeparator}c1$colSeparator",
    )
  }

  it should "append rows" in {
    val tb = new TextBuffer
    tb appendRow "first row"
    tb appendRow "second row"
    tb appendRow ""
    tb.format.linesWithSeparators.toList should contain theSameElementsInOrderAs List(
      "first row\n", "second row\n"
    )
  }

  it should "append rows and cols" in {
    val tb = new TextBuffer
    tb appendRow "first row"
    tb appendRow "little longer second row"
    tb.colMode()
    tb appendColumn
      """|a
         |b
         |c
         |""".stripMargin
    tb appendColumn
      """|a1
         |b1
         |c1
         |""".stripMargin
    tb.format.linesWithSeparators.toList should
      contain theSameElementsInOrderAs List(
      "first row\n",
      "little longer second row\n",
      s"a${colSeparator}a1$colSeparator\n",
      s"b${colSeparator}b1$colSeparator\n",
      s"c${colSeparator}c1$colSeparator",
    )

  }

