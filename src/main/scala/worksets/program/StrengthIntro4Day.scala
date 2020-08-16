package worksets.program

import worksets.Predef._
import worksets._
import worksets.calendar._
import worksets.support.IntPercentOps
import worksets.workouts.Dsl._

@SuppressWarnings(Array("org.wartremover.warts.All"))
class StrengthIntro4Day(implicit private val workoutHistory: WorkoutHistory) extends WorkoutGenerator {

  private val squatDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionSquat worksetByE1RM 87.pct x 1 worksetRelative 92.pct x 4 sets 3
        exercise CompetitionBench worksetByE1RM 78.pct x 3 loadDrop 3
        exercise WideGripBench worksetByE1RM 75.pct x 6 repeat 2
      )
  }

  private val deadLiftDay: WorkoutDay = { date =>
    (
      workout on date
        exercise CompetitionDeadlift worksetByE1RM 90.pct x 1 worksetRelative 93.pct x 3 sets 3
        exercise PendlayRowStandard workset 90.0.kg x 4 at Rpe8 worksetRelative 90.pct x 4 sets 3
      )
  }

  val weeklyProgram: WeeklyProgram = List(
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )
}