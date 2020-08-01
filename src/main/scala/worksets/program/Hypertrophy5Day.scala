package worksets.program

import worksets._
import worksets.Predef._
import worksets.calendar._
import worksets.workouts.Dsl._

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class Hypertrophy5Day(implicit private val workoutHistory: WorkoutHistory) extends WorkoutGenerator {


  private val squatDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionSquat workset 80.kg x 10 at Rpe6 worksetRelative 1.0 x 10 sets 3
        exercise CompetitionBench workset 70.kg x 10 at Rpe6 worksetRelative 1.0 x 10 sets 3
        exercise StandingPressStandard workset 55.kg x 6 at Rpe6 worksetRelative 1.0 x 6 sets 3
      )
  }

  private val deadLiftDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionDeadlift workset 110.kg x 6 at Rpe6 worksetRelative 1.1 x 6 sets 4
        exercise PendlayRowStandard workset 90.0.kg x 4 at Rpe8 worksetRelative 0.90 x 6 sets 3
        exercise WideGripBench workset 65.kg x 10 at Rpe7 worksetRelative 1.0 x 10 sets 3
      )
  }

  private val lightDay: WorkoutDay = { date =>
    (
      workout on date
        exercise FrontSquatStandard workset 80.kg x 10 at Rpe8 worksetRelative 0.92 x 10 sets 3
        exercise BodyWeightPullups workset 80.kg x 5 at Rpe7 worksetRelative 1.0 x 5 sets 3
      )
  }

  val weeklyProgram: WeeklyProgram = List(
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Wednesday -> lightDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )
}