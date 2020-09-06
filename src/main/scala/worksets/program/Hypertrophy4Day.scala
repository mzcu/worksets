package worksets.program

import worksets.Predef._
import worksets._
import worksets.calendar._
import worksets.support.IntPercentOps
import worksets.workouts.Dsl._

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class Hypertrophy4Day(implicit private val workoutHistory: WorkoutHistory) extends WorkoutGenerator {


  private val squatDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionSquat worksetByE1RM 70.pct x 8 repeat 3
        exercise CompetitionBench worksetByE1RM 70.pct x 8 repeat 3
        exercise WideGripBench worksetByE1RM 65.pct x 8 repeat 3
      )
  }

  private val deadLiftDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionDeadlift worksetByE1RM 80.pct x 6 repeat 3
        exercise PendlayRowStandard worksetByE1RM 80.pct x 4 repeat 3
        exercise BodyWeightPullups workset 80.kg x 5 at Rpe7 repeat 3
      )
  }

  val weeklyProgram: WeeklyProgram = List(
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )
}