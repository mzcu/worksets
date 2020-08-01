package worksets.cli

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

    println(s"${Bold.On(blockWeekNumber)} ${Underlined.On(currentProgram.programName)}\n")
    println(s"Weekly total volume: ${Bold.On(weeklyVolume.show)}")
    println(s"Average workout intensity: ${Bold.On(weeklyIntensity)}")

    val columnBuffer = new ColumnBuffer

    week.foreach { day =>
      columnBuffer.appendColumn(
        s"""
           |Date: \t${day.date.show}
           |Volume: \t${volume(day).show}
           |Avg RPE: \t${intensity(day).formatted("%.1f")}
           |
           |${day.show}
           |
           |""".stripMargin)
    }

    println(columnBuffer.format)

    if (StdIn.readLine("Save? y/[n]").toLowerCase.contains('y')) {
      ObjectStore.store(workoutHistory ++ week)
      println("Saved program for following week")
    } else {
      println("Back to the drawing board...")
    }
  }
}
