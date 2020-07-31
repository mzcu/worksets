package worksets.program

import java.time.LocalDate

import worksets.Syntax._
import worksets.calendar._
import worksets.workout.WorkoutBuilder.{workout}
import worksets.{Rpe6, Rpe7, Rpe8, Rpe9, Weight, Workout}
import worksets.workout.WorkoutDsl._

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class Hypertrophy5Day(implicit val workoutHistory: Seq[Workout]) extends WorkoutGenerator {

  import worksets.Predef._

  private val squatDay: LocalDate => Workout = { date: LocalDate =>
    (
      workout on date
        exercise CompetitionSquat workset 80.kg x 10 at Rpe6 worksetRelative 1.0 x 10 sets 3
        exercise CompetitionBench workset 70.kg x 10 at Rpe6 worksetRelative 1.0 x 10 sets 3
        exercise StandingPressStandard workset 55.kg x 6 at Rpe6 worksetRelative 1.0 x 6 sets 3
      )
  }

  private val deadLiftDay: LocalDate => Workout = { date: LocalDate =>
    (
      workout on date
        exercise CompetitionDeadlift workset 110.kg x 6 at Rpe6 worksetRelative 1.1 x 6 sets 4
        exercise PendlayRowStandard workset 90.0.kg x 4 at Rpe8 worksetRelative 0.90 x 6 sets 3
        exercise WideGripBench workset 65.kg x 10 at Rpe7 worksetRelative 1.0 x 10 sets 3
      )
  }

  private val lightDay: LocalDate => Workout = { date: LocalDate =>
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

object Hypertrophy5Day {
  def apply(implicit workoutHistory: Seq[Workout]): Hypertrophy5Day = new Hypertrophy5Day
}