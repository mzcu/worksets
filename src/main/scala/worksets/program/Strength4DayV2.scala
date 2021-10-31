package worksets.program

import worksets.Ops._
import worksets.Predef._
import worksets._
import worksets.calendar._
import worksets.support.pct
import worksets.workouts.Dsl._

object Strength4DayV2 extends WorkoutGenerator:

  private val squatDay: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 1 worksetByE1RM 8.rpe x 4 repeat 2
        exercise CompetitionBench worksetByE1RM 8.rpe x 1 worksetByE1RM 7.rpe x 4 repeat 3
        exercise BodyWeightDips workset 85.kg x 12 at 7.rpe repeat 2
      )

  private val volumePull: Workout = (
      workout
        exercise CompetitionDeadlift  worksetByE1RM 8.rpe x 4 repeat 3
        exercise PendlayRowStandard worksetByE1RM 7.rpe x 5 repeat 3
      )
  
  private val powerPull: Workout = (
    workout
      exercise SnatchPullStandard worksetByE1RM 8.rpe x 4 repeat 3
      exercise CompetitionDeadlift worksetByE1RM 8.rpe x 1
      exercise PendlayRowStandard worksetByE1RM 7.rpe x 5 repeat 3
    )


  val weeklyProgram: WeeklyProgram = List[(Day, Workout)](
    Monday -> squatDay,
    Tuesday -> volumePull,
    Thursday -> squatDay,
    Friday -> powerPull
  )
