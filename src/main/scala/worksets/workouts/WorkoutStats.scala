package worksets.workouts

import worksets._
import worksets.support.ListMonoid

/**
 * Created by on 04-01-20.
 */
object WorkoutStats {

  def volumePerExercise(workout: Workout): Seq[(ExerciseWithMods, Weight)] = {
    workout.sets.groupBy(_.exercise).map { case (exercise, worksets) =>
      val exerciseVolume = worksets.map(_.volume).combineAll
      (exercise, exerciseVolume)
    }.toList
  }

}
