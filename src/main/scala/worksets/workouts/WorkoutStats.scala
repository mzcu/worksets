package worksets.workouts

import worksets.{given _, _}
import worksets.support._

/**
 * Created by on 04-01-20.
 */
object WorkoutStats:

  def volumePerExercise(workout: Workout): Seq[(ExerciseWithMods, Weight)] =
    workout.sets.groupBy(_.exercise).map { case (exercise, worksets) =>
      val exerciseVolume = worksets.map(_.volume).combineAll()
      (exercise, exerciseVolume)
    }.toList

  def topSetPerExercise(workout: Workout): Seq[(ExerciseWithMods, worksets.Set)] =
    workout.sets.groupMapReduce(_.exercise)(_.actual)((a, b) => if a.weight.grams > b.weight.grams then a else b).toList

  def e1rmPerExercise(workout: Workout): Seq[(ExerciseWithMods, Weight)] =
    topSetPerExercise(workout).flatMap { case (exercise, set) =>
      set.rpe match
        case worksets.RpeUndefined => Seq.empty
        case RpeVal(_) => Seq((exercise, worksets.rpe.RpeOps.e1rm(set.weight, set.reps, set.rpe)))
    }

