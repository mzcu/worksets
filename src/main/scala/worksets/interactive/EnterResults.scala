package worksets.interactive

import worksets.{RpeVal, Weight}
import worksets.repository.ObjectStore

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object EnterResults extends App {

  import worksets.ConsoleView._
  import worksets.Show._


  val allWorkouts =  ObjectStore.load()
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
      val updatedSet: worksets.Set = actual.split(Array('x', '@')).toList match {
        case weight :: reps :: rpe :: Nil => worksets.Set(Weight(weight.toDouble), reps.toInt, RpeVal(rpe.toDouble))
        case _ => exercise.actual
      }
      exercise.copy(actual = updatedSet, completed = true)
    }

    val completed = firstOpenWorkoutSheet.copy(sets = completedSets)

    val updatedWorkout = allWorkouts.patch(firstOpenWorkoutIndex, Seq(completed), 1)
    ObjectStore.store(updatedWorkout)

    println("Worksheet closed")

  }



}
