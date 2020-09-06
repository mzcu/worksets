package worksets.program

import worksets.Predef._
import worksets._
import worksets.calendar._
import worksets.workouts.Dsl._

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class Hypertrophy4Day(implicit private val workoutHistory: WorkoutHistory) extends WorkoutGenerator {


  private val squatDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionSquat worksetByE1RM 8.rpe x 8 repeat 3
        exercise CompetitionBench worksetByE1RM 7.5.rpe x 8 repeat 3
        exercise WideGripBench worksetByE1RM 6.rpe x 9 repeat 3
      )
  }

  private val deadLiftDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionDeadlift worksetByE1RM 8.rpe x 6 repeat 3
        exercise PendlayRowStandard worksetByE1RM 8.rpe x 5 repeat 3
        exercise BodyWeightPullups workset 80.kg x 5 at 7.rpe repeat 3
      )
  }

  val weeklyProgram: WeeklyProgram = List(
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )
}