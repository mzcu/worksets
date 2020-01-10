package worksets.workout

import worksets.{RpeUndefined, RpeVal, Weight, WorkSet, Workout}

/**
 * Created by on 04-01-20.
 */
object WorkoutStats {
  def volume(workout: Workout): Weight =
    Weight(workout.sets.foldLeft(0)((acc: Int, set: WorkSet) => acc + (set.actual.weight.grams * set.actual.reps)))

  def intensity(workout: Workout): Double =
    workout.sets.foldLeft(0.0)((acc: Double, set: WorkSet) => acc + (set.actual.rpe match {
      case RpeUndefined => 0.0
      case RpeVal(value) => value
    })) / workout.sets.length
}
