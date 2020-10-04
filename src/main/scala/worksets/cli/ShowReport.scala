package worksets.cli

import worksets.calendar.YearWeekFormatter
import worksets.repository.ObjectStore
import worksets.support.TextBuffer
import worksets.workouts.WorkoutStats
import worksets.{WorkoutHistory, given _}

import scala.math.Ordering.Implicits.infixOrderingOps

/**
 * Created by on 03-01-20.
 */
object ShowReport:

  import ConsoleView.{given _}

  private def completedWorkouts: WorkoutHistory =
    val allWorkouts = ObjectStore.load()
    allWorkouts.filter(_.sets.exists(_.completed))

  def lastWorkouts(n: Int = 5): Unit =
    println(s"Last ${n.toString} workout(s)")
    completedWorkouts.map(_.show).foreach(Console.println)
  
  def weeklyVolumePerExercise(workouts: Int = 30) =
    completedWorkouts.takeRight(workouts).flatMap { w =>
      val weekString = w.date.format(YearWeekFormatter)
      WorkoutStats.volumePerExercise(w).map(v => (v._1, weekString, v._2))
    }.groupBy(_._1)
    
  def showWeeklyVolume() =
    val weeklyVolume = weeklyVolumePerExercise(30).values.flatMap(_.map(v => (v._2, v._3))).groupMapReduce(_._1)(_._2)(_ + _).toList
    weeklyVolume.sortBy(_._1).foreach((wk, weight) => println(s"$wk: ${weight.show}"))

  def volumeProgression(workouts: Int = 10): Unit =

    val textBuffer = new TextBuffer
    textBuffer.colMode()

    val volumePerExercise = weeklyVolumePerExercise(workouts)

    val exerciseVolumePerWeek = volumePerExercise.map { case (exercise, volumeData) =>
      val weekGroups = volumeData.groupMapReduce(_._2)(_._3)(_ + _).toList.sortBy(_._1).map(t => s"${t._1}: ${t._2.show}").mkString("\n")
      s"${exercise.show}\n$weekGroups"
    }.toList

    exerciseVolumePerWeek.foreach(textBuffer.appendColumn)

    println(textBuffer.format)


  def e1rm(workouts: Int = 10): Unit =

    val textBuffer = new TextBuffer
    textBuffer.colMode()

    val e1rmPerExercise = completedWorkouts.takeRight(workouts).flatMap { w =>
      val weekString = w.date.format(YearWeekFormatter)
      WorkoutStats.e1rmPerExercise(w).map(v => (v._1, weekString, v._2))
    }.groupBy(_._1)

    val e1rmPerWeek = e1rmPerExercise.map { case (exercise, e1rmData) =>
      val weekGroups = e1rmData.groupMapReduce(_._2)(_._3)(_ max _).toList.sortBy(_._1).map(t => s"${t._1}: ${t._2.show}").mkString("\n")
      s"${exercise.show}\n$weekGroups"
    }.toList

    e1rmPerWeek.foreach(textBuffer.appendColumn)

    println(textBuffer.format)

  def main(args: Array[String]): Unit =
    println("E1RM per exercise")
    e1rm(20)
    println("Volume per exercise")
    volumeProgression(20)
    println("Weekly volume")
    showWeeklyVolume()

