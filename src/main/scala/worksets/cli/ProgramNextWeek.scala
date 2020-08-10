package worksets.cli

import java.time.{Instant, LocalDate}

import fansi._
import worksets.WorkoutHistory
import worksets.calendar.YearWeekFormatter
import worksets.program.{Hypertrophy5Day, StrengthIntro4Day, WorkoutGenerator}
import worksets.report.{Browser, FilePublisher}
import worksets.repository.ObjectStore
import worksets.support.{ListMonoid, TextBuffer}

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object ProgramNextWeek {
  def run(): Unit = {

    implicit val workoutHistory: WorkoutHistory = ObjectStore.load()

    // Set desired program here
    val currentProgram: WorkoutGenerator = new StrengthIntro4Day

    val startDate = LocalDate.now()
    val week = currentProgram.generate(startDate).toList
    val blockWeekNumber = week.head.date.format(YearWeekFormatter)
    val weeklyVolume = week.map(_.volume).combineAll
    val weeklyIntensity = {
      val weeklyIntensity = week.map(_.intensity)
      (weeklyIntensity.sum / weeklyIntensity.size).formatted("%.2f")
    }

    import ConsoleView._
    import Show._

    val textBuffer = new TextBuffer

    textBuffer.appendRow(s"${Bold.On(blockWeekNumber)} ${Underlined.On(currentProgram.programName)}")
    textBuffer.appendRow("")
    textBuffer.appendRow(s"Weekly total volume: ${Bold.On(weeklyVolume.show)}")
    textBuffer.appendRow(s"Average workout intensity: ${Bold.On(weeklyIntensity)}")
    textBuffer.appendRow("")
    textBuffer.appendRow("")

    textBuffer.colMode()

    week.foreach { day =>
      textBuffer.appendColumn(
        s"""
           |Date: \t${day.date.show}
           |Volume: \t${day.volume.show}
           |Avg RPE: \t${day.intensity.formatted("%.1f")}
           |
           |${day.show}
           |
           |""".stripMargin)
    }

    val workoutPlan = textBuffer.format
    println(workoutPlan)

    if (StdIn.readLine("Save? y/[n]").toLowerCase.contains('y')) {
      val publishedTo = FilePublisher.publish(blockWeekNumber + "_" + Instant.now(), workoutPlan)
      ObjectStore.store(workoutHistory ++ week)
      println("Saved program for following week. Opening report...")
      Thread.sleep(1500)
      Browser.browse(publishedTo.toUri)
    } else {
      println("Back to the drawing board...")
    }
  }
}
