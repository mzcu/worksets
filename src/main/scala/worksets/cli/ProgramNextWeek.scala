package worksets.cli

import worksets.program.{Hypertrophy5Day, WorkoutGenerator}
import worksets.repository.ObjectStore
import worksets.workout.WorkoutStats
import WorkoutStats._

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object ProgramNextWeek extends App {
  val workoutHistory: Seq[worksets.Workout] = ObjectStore.load()
  val currentProgram: WorkoutGenerator = Hypertrophy5Day(workoutHistory)
  val week = currentProgram.generate()
  val weeklyVolume = week.map(volume).reduce(_ + _)
  val weeklyIntensity = {
    val weeklyIntensity = week.map(intensity)
    (weeklyIntensity.sum / weeklyIntensity.size).formatted("%.2f")
  }

  import Show._
  import ConsoleView._

  println(s"Weekly total volume: ${weeklyVolume.show}")
  println(s"Average workout intensity: $weeklyIntensity")

  implicit val buf: ListBuffer[ListBuffer[Char]] = ListBuffer()

  def bufferColumns(block: String): Unit = {
    var maxLen = Int.MinValue
    block.linesWithSeparators.map(_.stripLineEnd).zipWithIndex.foreach { case (line, i) =>
      if (buf.size == i) buf.append(ListBuffer())
      buf(i).appendAll(line)
      val currentLineLength = buf(i).size
      maxLen = if (currentLineLength > maxLen) currentLineLength else maxLen
    }
    buf.mapInPlace(_.padToInPlace(maxLen + 10, ' ')).mapInPlace(_.append('\t'))
  }

  def printColumns(buf: ListBuffer[ListBuffer[Char]]): Unit = buf.foreach { line =>
      println(line.mkString(""))
    }

  week.foreach { day =>
    bufferColumns(
      s"""
         |Date: \t${day.date.show}
         |Volume: \t${volume(day).show}
         |Avg RPE: \t${intensity(day).formatted("%.1f")}
         |
         |${day.show}
         |
         |""".stripMargin)
  }

  printColumns(buf)

  if(StdIn.readLine("Save? y/[n]").toLowerCase.contains('y')) {
    ObjectStore.store(workoutHistory ++ week)
    println("Saved program for following week")
  } else {
    println("Back to the drawing board...")
  }

}
