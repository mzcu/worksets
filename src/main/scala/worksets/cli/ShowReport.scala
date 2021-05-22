package worksets.cli

import java.text.{DecimalFormat, DecimalFormatSymbols}

import com.mitchtalmadge.asciidata.graph.ASCIIGraph
import worksets.calendar.YearWeekFormatter
import worksets.repository.ObjectStore
import worksets.support.TextBuffer
import worksets.workouts.WorkoutStats
import worksets.{WorkoutHistory, given}

import scala.math.Ordering.Implicits.infixOrderingOps

/**
 * Created by on 03-01-20.
 */
object ShowReport:

  import ConsoleView.{given, *}

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


  def weeklyDifficultyPerExercise(workouts: Int = 30) =
    completedWorkouts.takeRight(workouts).flatMap { w =>
      val weekString = w.date.format(YearWeekFormatter)
      WorkoutStats.averageDifficultyPerExercise(w).map(v => (v._1, weekString, v._2))
    }.groupBy(_._1)


  def weeklyIntensityPerExercise(workouts: Int = 30) =
    completedWorkouts.takeRight(workouts).flatMap { w =>
      val weekString = w.date.format(YearWeekFormatter)
      WorkoutStats.maxIntensityPerExercise(w).map(v => (v._1, weekString, v._2 * 100))
    }.groupBy(_._1)
    
  def weeklyVolume(): String =
    chartSection("Weekly vol.") { (chartHeight, textBuffer) =>
      val weeklyVolume = weeklyVolumePerExercise(100).values.flatMap(_.map(v => (v._2, v._3))).groupMapReduce(_._1)(_._2)(_ + _).toList.sortBy(_._1)
      val from = weeklyVolume.head._1
      val to = weeklyVolume.last._1
      val chart = ASCIIGraph.fromSeries(weeklyVolume.map(_._2.grams / 1000.0).toArray).withTickFormat(new DecimalFormat("#")).withNumRows(chartHeight).plot()
      textBuffer.appendColumn(s" $from .. $to\n$chart")
    }

  def chartSection(title: String, chartHeight: Int = 5)(chartMaker: (Int, TextBuffer) => Unit): String =
    val textBuffer = new TextBuffer
    textBuffer.colMode()
    val spacer = "\n".repeat(chartHeight/2)
    textBuffer.appendColumn( s"$spacer\n${title.padTo(12, ' ')}\n$spacer")
    chartMaker(chartHeight, textBuffer)
    textBuffer.format

  def intensity(workouts: Int = 10): String =
    chartSection("Intensity") { (chartHeight, textBuffer) =>
      val intensityPerExercise = weeklyIntensityPerExercise(workouts)
      val exerciseVolumePerWeek = intensityPerExercise.map { case (exercise, volumeData) =>
        val weekGroups = volumeData.groupMap(_._2)(_._3).view.mapValues(xs => xs.sum / xs.length)
          .toList.sortBy(_._1).map(t => s"${t._1}: ${t._2}").mkString("\n")
        s"${exercise.show}\n$weekGroups"
      }.toList
      intensityPerExercise.foreach { e =>
        val chart = ASCIIGraph.fromSeries(e._2.map(_._3).toArray).withTickFormat(new DecimalFormat("#")).withNumRows(chartHeight).plot()
        textBuffer.appendColumn(s"${e._1.show}\n$chart")
      }
    }
  
  def difficulty(workouts: Int = 10): String =
   chartSection("Difficulty") { (chartHeight, textBuffer) => 
     val difficultyPerExercise = weeklyDifficultyPerExercise(workouts)
     val exerciseVolumePerWeek = difficultyPerExercise.map { case (exercise, volumeData) =>
       val weekGroups = volumeData.groupMap(_._2)(_._3).view.mapValues { xs => xs.sum / xs.length }
         .toList.sortBy(_._1).map(t => s"${t._1}: ${t._2}").mkString("\n")
       s"${exercise.show}\n$weekGroups"
     }.toList
     difficultyPerExercise.foreach { e =>
       val chart = ASCIIGraph.fromSeries(e._2.map(_._3).toArray).withTickFormat(new DecimalFormat("#")).withNumRows(chartHeight).plot()
       textBuffer.appendColumn(s"${e._1.show}\n$chart")
     }
   }

  def volume(workouts: Int = 10): String =
    chartSection("Volume") { (chartHeight, textBuffer) =>
      val volumePerExercise = weeklyVolumePerExercise(workouts)
      val exerciseVolumePerWeek = volumePerExercise.map { case (exercise, volumeData) =>
        val weekGroups = volumeData.groupMapReduce(_._2)(_._3)(_ + _).toList.sortBy(_._1).map(t => s"${t._1}: ${t._2.show}").mkString("\n")
        s"${exercise.show}\n$weekGroups"
      }.toList
      volumePerExercise.foreach { e =>
        val chart = ASCIIGraph.fromSeries(e._2.map(_._3.grams / 1000.0).toArray).withTickFormat(new DecimalFormat("#")).withNumRows(chartHeight).plot()
        textBuffer.appendColumn(s"${e._1.show}\n$chart")
      }
    }


  def e1rm(workouts: Int = 10): String =
    chartSection("E1RM") { (chartHeight, textBuffer) =>
      val e1rmPerExercise = completedWorkouts.takeRight(workouts).flatMap { w =>
        val weekString = w.date.format(YearWeekFormatter)
        WorkoutStats.e1rmPerExercise(w).map(v => (v._1, weekString, v._2))
      }.groupBy(_._1)
      e1rmPerExercise.foreach { e =>
        val chart = ASCIIGraph.fromSeries(e._2.map(_._3.grams / 1000.0).toArray).withTickFormat(new DecimalFormat("#")).withNumRows(chartHeight).plot()
        textBuffer.appendColumn(s"${e._1.show}\n$chart")
      }
    }


  def main(args: Array[String]): Unit =
    val workouts = 28
    println(volume(workouts))
    println()
    println(intensity(workouts))
    println()
    println(difficulty(workouts))
    println()
    println(e1rm(workouts))
    println()
    println(weeklyVolume())
