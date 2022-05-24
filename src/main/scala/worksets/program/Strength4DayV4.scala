package worksets.program

import worksets.*
import worksets.Ops.*
import worksets.Predef.*
import worksets.calendar.*
import worksets.support.pct
import worksets.workouts.Dsl.*

/**
 * Strength4DayV3 with following adjustments:
 *  - Starting new block lighter
 *  - Less total workload
 *  - Deadlift has priority over squat
 *  - A bit lower intensity / more reps for core exercises
 */
object Strength4DayV4 extends WorkoutGenerator:

  private val volumeSquat: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 6 repeat 3
        exercise TempoBench300 worksetByE1RM  8.rpe x 5 repeat 3
        exercise CloseGripBench worksetByE1RM 7.rpe x 8 repeat 2
      )


  private val heavySquat: Workout = (
    workout
      exercise CompetitionSquat worksetByE1RM 8.rpe x 1
      exercise TempoSquat300 workset 90.kg x 6 at 7.rpe repeat 2
      exercise CompetitionBench workset 110.kg x 1 at 7.rpe
      exercise TempoBench300 worksetByE1RM  8.rpe x 5 repeat 2
    )

  private val volumePull: Workout = (
      workout
        exercise CompetitionDeadlift  worksetByE1RM 8.rpe x 4 repeat 3
        exercise PendlayRowStandard worksetByE1RM 7.rpe x 5 repeat 3
      )
  
  private val heavyPull: Workout = (
    workout
      exercise CompetitionDeadlift worksetByE1RM 8.rpe x 1
      exercise PausedDeadlift worksetByE1RM 7.rpe x 5 repeat 2
      exercise WeightedPullups worksetByE1RM  8.rpe x 5 repeat 2 // approx bw + added weight
    )


  val weeklyProgram: WeeklyProgram = List[(Day, Workout)](
    Monday -> heavyPull,
    Tuesday -> heavySquat,
    Thursday -> volumePull,
    Friday -> volumeSquat
  )
