package worksets.cli

import java.time.{Instant, LocalDate}

import fansi._
import worksets.{Config, Weight, support}
import worksets.calendar.YearWeekFormatter
import worksets.report.{Browser, FilePublisher}
import worksets.repository.ObjectStore
import worksets.support.{TextBuffer}

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.StringPlusAny"))
object ProgramNextWeek extends Config:
  def main(args: Array[String]): Unit =
    val startDate = LocalDate.now()
    val week = workoutGenerator.generate(startDate).toList
    @SuppressWarnings(Array("org.wartremover.warts.TraversableOps"))
    val blockWeekNumber = week.head.date.format(YearWeekFormatter)
    val weeklyVolume: Weight = week.map(_.volume).reduceLeft((l,r) => Weight(l.grams + r.grams))
    val weeklyDifficulty =
      val weeklyIntensity = week.map(_.difficulty)
      (weeklyIntensity.sum / weeklyIntensity.size).formatted("%.2f")

    import ConsoleView.{given, *}

    val textBuffer = new TextBuffer

    textBuffer.appendRow(s"${Bold.On(blockWeekNumber)} ${Underlined.On(workoutGenerator.programName)}")
    textBuffer.appendRow("")
    textBuffer.appendRow(s"Weekly total volume: ${Bold.On(weeklyVolume.show)}")
    textBuffer.appendRow(s"Average workout difficulty: ${Bold.On(weeklyDifficulty)}")
    textBuffer.appendRow("")
    textBuffer.appendRow("")

    textBuffer.colMode()

    week.foreach { day =>
      textBuffer.appendColumn(
        s"""
           |Date:    ${day.date.show}
           |Volume:  ${day.volume.show}
           |Avg RPE: ${day.difficulty.formatted("%.1f")}
           |
           |${day.show}
           |
           |
           |
           |
           |
           |
           |""".stripMargin)
    }

    val workoutPlan = textBuffer.format
    println(workoutPlan)

    if StdIn.readLine("Save? y/[n]").toLowerCase.contains('y') then
      val publishedTo = FilePublisher.publish(blockWeekNumber + "_" + Instant.now(), workoutPlan)
      val _ = ObjectStore.store(workoutGenerator.workoutHistory ++ week)
      println("Saved program for following week. Opening report...")
      Thread.sleep(1500)
      Browser.browse(publishedTo.toUri)
    else
      println("Back to the drawing board...")
