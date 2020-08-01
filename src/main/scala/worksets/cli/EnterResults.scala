package worksets.cli

import worksets.repository.ObjectStore
import worksets.{RpeVal, Weight}

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object EnterResults {

  import ConsoleView._
  import Show._

  def interact(): Unit = {
    val allWorkouts = ObjectStore.load()
    val firstOpenWorkoutIndex = allWorkouts.indexWhere(_.sets.exists(!_.completed))

    if (firstOpenWorkoutIndex < 0) {
      println("No open workouts, exiting...")
    } else {

      val firstOpenWorkoutSheet = allWorkouts(firstOpenWorkoutIndex)

      println(s"\n\nWorkout sheet for ${firstOpenWorkoutSheet.date}")

      val completedSets = firstOpenWorkoutSheet.sets.map { exercise =>
        println(exercise.exercise.show)
        print("Target: ")
        println(exercise.target.show)
        val actual = StdIn.readLine("> ")
        val updatedSet = actual.split(Array('x', '@')).toList match {
          case weight :: reps :: rpe :: Nil => worksets.Set(Weight(weight.toDouble), reps.toInt, RpeVal(rpe.toDouble))
          case "-" :: Nil => worksets.Set.empty // skipped a set
          case _ => exercise.actual // as planned
        }
        exercise.copy(actual = updatedSet, completed = true)
      }

      val completed = firstOpenWorkoutSheet.copy(sets = completedSets)

      val updatedWorkout = allWorkouts.patch(firstOpenWorkoutIndex, Seq(completed), 1)
      ObjectStore.store(updatedWorkout)

      println("Worksheet closed")
    }
  }

}
