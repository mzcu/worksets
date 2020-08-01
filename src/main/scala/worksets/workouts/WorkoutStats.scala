package worksets.workouts

import worksets._

/**
 * Created by on 04-01-20.
 */
object WorkoutStats {

  def volume(workout: Workout): Weight =
    workout.sets.foldLeft(0.kg)((acc: Weight, set: WorkSet) => acc + (set.actual.weight * set.actual.reps))

  def intensity(workout: Workout): Double =
    workout.sets.foldLeft(0.0)((acc: Double, set: WorkSet) => acc + (set.actual.rpe match {
      case RpeUndefined => 0.0
      case RpeVal(value) => value
    })) / workout.sets.length

  def volumePerExercise(workout: Workout): Seq[(ExerciseWithMods, Weight)] = {
    workout.sets.groupBy(_.exercise).map { case (exercise, worksets) =>
      val exerciseVolume = worksets.foldLeft(0.kg)((acc: Weight, set: WorkSet) => acc + set.actual.weight * set.actual.reps)
      (exercise, exerciseVolume)
    }.toList
  }
}
