package worksets.program

import worksets.*
import worksets.Ops.*
import worksets.Predef.*
import worksets.calendar.*
import worksets.support.pct
import worksets.workouts.Dsl.*

/**
 * Strength4DayV2 with following adjustments:
 *  - changes in exercises targeting chest due to pectoralis minor pain
 *  - replaced snatch pull with deficit DL due to staleness
 */
object Strength4DayV3 extends WorkoutGenerator:

  private val squatDay: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 1 worksetByE1RM 8.rpe x 4 repeat 2
        exercise CompetitionBench worksetByE1RM 8.rpe x 1 worksetByE1RM 7.rpe x 4 repeat 2
        exercise CloseGripBench workset 75.kg x 8 at 7.rpe repeat 2
        // exercise BodyWeightDips workset 85.kg x 12 at 7.rpe repeat 2
      )

  private val volumePull: Workout = (
      workout
        exercise CompetitionDeadlift  worksetByE1RM 8.rpe x 4 repeat 3
        exercise PendlayRowStandard worksetByE1RM 7.rpe x 5 repeat 3
      )
  
  private val powerPull: Workout = (
    workout
      exercise DeficitDeadlift workset 100.kg x 5 at 7.rpe repeat 3
      exercise CompetitionDeadlift worksetByE1RM 8.rpe x 1
      exercise PendlayRowStandard worksetByE1RM 7.rpe x 5 repeat 3
    )


  val weeklyProgram: WeeklyProgram = List[(Day, Workout)](
    Monday -> squatDay,
    Tuesday -> volumePull,
    Thursday -> squatDay,
    Friday -> powerPull
  )
