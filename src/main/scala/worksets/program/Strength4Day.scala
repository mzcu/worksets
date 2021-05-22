package worksets.program

import worksets.Ops._
import worksets.Predef._
import worksets._
import worksets.calendar._
import worksets.support.pct
import worksets.workouts.Dsl._

object Strength4Day extends WorkoutGenerator {

  private val squatDay: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 1 worksetByE1RM 8.rpe x 5 repeat 2
        exercise CompetitionBench worksetByE1RM 6.rpe x 1 worksetByE1RM 6.rpe x 5 repeat 2
      )

  private val deadLiftDay: Workout = (
      workout
        exercise CompetitionDeadlift worksetByE1RM 8.rpe x 1 worksetByE1RM 7.rpe x 4 repeat 2
        exercise PendlayRowStandard worksetByE1RM 8.rpe x 6 repeat 3
      )

  val weeklyProgram: WeeklyProgram = List[(Day, Workout)](
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )
}