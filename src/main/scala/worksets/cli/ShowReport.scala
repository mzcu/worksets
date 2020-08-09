package worksets.cli

import worksets.WeightIsAQuantity
import worksets.WorkoutHistory
import worksets.calendar.YearWeekFormatter
import worksets.repository.ObjectStore
import worksets.support.TextBuffer
import worksets.workouts.WorkoutStats

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object ShowReport {

  import ConsoleView._
  import Show._

  private def completedWorkouts: WorkoutHistory = {
    val allWorkouts = ObjectStore.load()
    allWorkouts.filter(_.sets.exists(_.completed))
  }

  def lastWorkouts(n: Int = 5): Unit = {
    println(s"Last $n workout(s)")
    completedWorkouts.map(_.show).foreach(Console.println)
  }

  def volumeProgression(workouts: Int = 5): Unit = {

    val textBuffer = new TextBuffer
    textBuffer.colMode()

    val volumePerExercise = completedWorkouts.takeRight(workouts).flatMap { w =>
      val weekString = w.date.format(YearWeekFormatter)
      WorkoutStats.volumePerExercise(w).map(v => (v._1, weekString, v._2))
    }.groupBy(_._1)

    val exerciseVolumePerWeek = volumePerExercise.map { case (exercise, volumeData) =>
      val weekGroups = volumeData.groupMapReduce(_._2)(_._3)(_ + _).toList.sortBy(_._1).map(t => s"${t._1}: ${t._2.show}").mkString("\n")
      s"${exercise.show}\n$weekGroups"
    }.toList

    exerciseVolumePerWeek.foreach(textBuffer.appendColumn)

    println(textBuffer.format)

  }

  def e1rm(workouts: Int = 5): Unit = {

    val textBuffer = new TextBuffer
    textBuffer.colMode()

    val e1rmPerExercise = completedWorkouts.takeRight(workouts).flatMap { w =>
      val weekString = w.date.format(YearWeekFormatter)
      WorkoutStats.e1rmPerExercise(w).map(v => (v._1, weekString, v._2))
    }.groupBy(_._1)

    val e1rmPerWeek = e1rmPerExercise.map { case (exercise, e1rmData) =>
      val weekGroups = e1rmData.groupMapReduce(_._2)(_._3)(_ + _).toList.sortBy(_._1).map(t => s"${t._1}: ${t._2.show}").mkString("\n")
      s"${exercise.show}\n$weekGroups"
    }.toList

    e1rmPerWeek.foreach(textBuffer.appendColumn)

    println(textBuffer.format)
  }

  def main(args: Array[String]): Unit = {
    e1rm()
  }

}
