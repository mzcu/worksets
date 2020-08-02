package worksets.cli

import java.nio.file.{Files, Paths}
import java.time.LocalDate

import worksets.WorkoutHistory
import worksets.program.{Hypertrophy5Day, WorkoutGenerator}
import worksets.repository.ObjectStore
import worksets.workouts.WorkoutStats._
import fansi._
import worksets.calendar.YearWeekFormatter

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object ProgramNextWeek {
  def run(): Unit = {

    implicit val workoutHistory: WorkoutHistory = ObjectStore.load()

    // Set desired program here
    val currentProgram: WorkoutGenerator = new Hypertrophy5Day

    val startDate = LocalDate.now()
    val week = currentProgram.generate(startDate)
    val blockWeekNumber = week.head.date.format(YearWeekFormatter)
    val weeklyVolume = week.map(volume).reduce(_ + _)
    val weeklyIntensity = {
      val weeklyIntensity = week.map(intensity)
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
           |Volume: \t${volume(day).show}
           |Avg RPE: \t${intensity(day).formatted("%.1f")}
           |
           |${day.show}
           |
           |""".stripMargin)
    }

    val workoutPlan = textBuffer.format
    println(workoutPlan)
    // TODO: store in printable format
    //Files.write(Files.createFile(Paths.get("/tmp/adsas.txt")), workoutPlan.getBytes())

    if (StdIn.readLine("Save? y/[n]").toLowerCase.contains('y')) {
      ObjectStore.store(workoutHistory ++ week)
      println("Saved program for following week")
    } else {
      println("Back to the drawing board...")
    }
  }
}
