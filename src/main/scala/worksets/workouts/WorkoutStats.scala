package worksets.workouts

import worksets.{given, *}
import worksets.support.{given, *}
import worksets.rpe.RpeOps.toPct

/**
 * Created by on 04-01-20.
 */
object WorkoutStats:

  def volumePerExercise(workout: Workout): Seq[(ExerciseWithMods, Weight)] =
    workout.sets.groupBy(_.exercise).map { case (exercise, worksets) =>
      val exerciseVolume = worksets.map(_.volume).combineAll()
      (exercise, exerciseVolume)
    }.toList
  
  def averageDifficultyPerExercise(workout: Workout): Seq[(ExerciseWithMods, Double)] =
    workout.sets.groupBy(_.exercise).map { case (exercise, worksets) =>
      val exerciseDifficulty = worksets.map(_.difficulty).filter(_ > 0)
      val averageDifficulty = if exerciseDifficulty.length > 0 then 
        exerciseDifficulty.sum / exerciseDifficulty.length
      else 
        0
      (exercise, averageDifficulty)
    }.toList

  def maxIntensityPerExercise(workout: Workout): Seq[(ExerciseWithMods, Double)] =
    workout.sets.groupBy(_.exercise).map { case (exercise, worksets) =>
      val maxIntensity = worksets.map(_.actual).map { w => toPct(w.reps, w.rpe )}.max
      (exercise, maxIntensity)
    }.toList
    
  def topSetPerExercise(workout: Workout): Seq[(ExerciseWithMods, worksets.Set)] =
    workout.sets.groupMapReduce(_.exercise)(_.actual)((a, b) => if a.weight.grams > b.weight.grams then a else b).toList

  def e1rmPerExercise(workout: Workout): Seq[(ExerciseWithMods, Weight)] =
    topSetPerExercise(workout).flatMap { case (exercise, set) =>
      set.rpe match
        case worksets.RpeUndefined => Seq.empty
        case RpeVal(_) => Seq((exercise, worksets.rpe.RpeOps.e1rm(set.weight, set.reps, set.rpe)))
    }

