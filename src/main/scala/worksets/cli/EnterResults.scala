package worksets.cli

import worksets.parser.{ExerciseScope, SetLiteral, SetScope, WorkoutParserError, WorksetMod, SetUnchanged}
import worksets.repository.ObjectStore

import scala.io.StdIn

/**
 * Created by on 03-01-20.
 */
object EnterResults:

  import ConsoleView.{given, *}

  def main(args: Array[String]): Unit =

    val workoutInput = WorkoutInput
    val allWorkouts = ObjectStore.load()
    val firstOpenWorkoutIndex = allWorkouts.indexWhere(_.sets.exists(!_.completed))

    if firstOpenWorkoutIndex < 0 then
      println("No open workouts, exiting...")
    else

      val firstOpenWorkoutSheet = allWorkouts(firstOpenWorkoutIndex)
      println(s"\n\nWorkout sheet for ${firstOpenWorkoutSheet.date.toString}")
      
      val workoutCursor: WorkoutCursor = WorkoutCursor(firstOpenWorkoutSheet)
      workoutCursor.foreach { exercise =>
        println(exercise.exercise.show)
        print("Target: ")
        println(exercise.target.show)
        val actual = workoutInput.reader.readLine("> ", null)
        val updatedSet = workoutInput.parser.parseLine(actual) match
          case SetLiteral(set) => workoutCursor.accept(set)
          case mod @ WorksetMod(_, _) => workoutCursor.accept(mod)
          case SetUnchanged => workoutCursor.accept(exercise.actual)
          case WorkoutParserError(msg) =>
            Console.err.println(s"Parse error, aborting. Message: '$msg'")
            return
      }

      val completed = workoutCursor.get
      val updatedWorkout = allWorkouts.patch(firstOpenWorkoutIndex, Seq(completed), 1)

      println(completed.show)
      if StdIn.readLine("Save? y/[n]").toLowerCase.contains('y') then
        val _ = ObjectStore.store(updatedWorkout)
        println("Worksheet closed")
      else
        println("Aborted")


class WorkoutCursor(workoutArg: worksets.Workout) extends Iterator[worksets.WorkSet]:

  private var index = -1
  private var workout = workoutArg

  override def hasNext: Boolean = workout.sets.length > index + 1
  override def next(): worksets.WorkSet =
    if hasNext then
      index += 1
      workout.sets(index)
    else
      Iterator.empty.next()

  def get: worksets.Workout = workout
  
  def accept(set: worksets.Set) =
    val updatedSets = Seq(workout.sets(index).copy(actual = set, completed = true))
    val updatedWorkSets = workout.sets.patch(index, updatedSets, 1)
    workout = workout.copy(sets = updatedWorkSets)
  
  def accept(mod: WorksetMod) =
    val update: List[worksets.WorkSet] = mod.scope match {
      case ExerciseScope =>
        val currentExercise = workout.sets(index).exercise
        val currentExerciseSets =
          workout.sets.slice(index, workout.sets.size).takeWhile(_.exercise == currentExercise)
        val updatedExerciseSets = currentExerciseSets.map { currentWorkSet =>
          val modifiedSet = applyMod(mod, currentWorkSet)
          currentWorkSet.copy(actual = modifiedSet, completed = true)
        }
        updatedExerciseSets
      case SetScope =>
        val currentSet = workout.sets(index)
        val updatedSet: worksets.Set = applyMod(mod, currentSet)
        val completedSet = currentSet.copy(actual = updatedSet, completed = true)
        List(completedSet)
    }
    val updatedWorkSets = workout.sets.patch(index, update, update.size)
    index += update.size - 1
    workout = workout.copy(sets = updatedWorkSets)

  private def applyMod(mod: WorksetMod, currentSet: worksets.WorkSet) =
    mod.mods.foldLeft(currentSet.actual)((s, mod) => mod.modify(s))



