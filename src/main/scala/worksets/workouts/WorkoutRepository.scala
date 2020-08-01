package worksets.workouts

import worksets.WorkoutHistory

/**
 * Created by on 31-07-20.
 */
trait WorkoutRepository extends CanReadWorkouts with CanUpdateWorkouts

trait CanUpdateWorkouts {
  def store(workouts: WorkoutHistory): Int
}

trait CanReadWorkouts {
  def load(): WorkoutHistory
}
