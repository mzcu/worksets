package worksets

import worksets.program.WorkoutGenerator

/**
 * Created by on 13-09-20.
 */
trait Config:
  val workoutGenerator: WorkoutGenerator = program.Strength4DayV4
