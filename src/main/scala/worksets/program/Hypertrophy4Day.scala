package worksets.program

import worksets._
import worksets.Ops._
import worksets.Predef._
import worksets.calendar.{Day, Monday, Tuesday, Thursday, Friday}
import worksets.workouts.Dsl._

/**
 * Created by on 22-07-20.
 */
object Hypertrophy4Day extends WorkoutGenerator {
  val weeklyProgram: WeeklyProgram =
    val squatDay: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 8 repeat 3
        exercise CompetitionBench worksetByE1RM 7.5.rpe x 8 repeat 3
        exercise WideGripBench worksetByE1RM 6.rpe x 9 repeat 4
      )
    val deadLiftDay: Workout = (
      workout
        exercise CompetitionDeadlift worksetByE1RM 8.rpe x 6 repeat 3
        exercise PendlayRowStandard worksetByE1RM 8.rpe x 5 repeat 3
        exercise BodyWeightPullups workset 80.kg x DynamicReps.AddOne at 7.rpe repeat 3
      )
    List[(Day, Workout)](
      Monday -> squatDay,
      Tuesday -> deadLiftDay,
      Thursday -> squatDay,
      Friday -> deadLiftDay
    )
}