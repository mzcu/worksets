package worksets.workout

import worksets.Syntax.IntOps
import worksets.{RpeUndefined, RpeVal, Weight, WorkSet, Workout}

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
}
