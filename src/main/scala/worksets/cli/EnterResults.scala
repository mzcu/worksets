package worksets.cli

import worksets.parser.{SetLiteral, WorkoutParserError, WorksetMod}
import worksets.repository.ObjectStore

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
object EnterResults {

  import ConsoleView._
  import Show._

  def interact(): Unit = {


    val workoutInput = WorkoutInput
    val allWorkouts = ObjectStore.load()
    val firstOpenWorkoutIndex = allWorkouts.indexWhere(_.sets.exists(!_.completed))

    if (firstOpenWorkoutIndex < 0) {
      println("No open workouts, exiting...")
    } else {

      val firstOpenWorkoutSheet = allWorkouts(firstOpenWorkoutIndex)

      println(s"\n\nWorkout sheet for ${firstOpenWorkoutSheet.date.toString}")

      val completedSets = firstOpenWorkoutSheet.sets.map { exercise =>
        println(exercise.exercise.show)
        print("Target: ")
        println(exercise.target.show)
        @SuppressWarnings(Array("org.wartremover.warts.Null"))
        val actual = workoutInput.reader.readLine("> ", null, s"${exercise.target.show}")
        val updatedSet = workoutInput.parser.parseLine(actual) match {
          case SetLiteral(set) => set
          case WorksetMod(_, mods) => mods.foldLeft(exercise.actual)((s, mod) => mod.modify(s))
          case WorkoutParserError(msg) =>
            Console.err.println(s"Parse error, will not change the set. Message: '$msg'")
            exercise.actual
        }
        exercise.copy(actual = updatedSet, completed = true)
      }

      val completed = firstOpenWorkoutSheet.copy(sets = completedSets)
      val updatedWorkout = allWorkouts.patch(firstOpenWorkoutIndex, Seq(completed), 1)

      println(completed.show)
      if (StdIn.readLine("Save? y/[n]").toLowerCase.contains('y')) {
        val _ = ObjectStore.store(updatedWorkout)
        println("Worksheet closed")
      } else {
        println("Aborted")
      }

    }
  }

}
