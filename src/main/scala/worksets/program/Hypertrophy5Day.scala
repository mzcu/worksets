package worksets.program

import java.time.LocalDate

import worksets.calendar._
import worksets.workout.WorkoutBuilder.newWorkout
import worksets.{Rpe6, Rpe7, Rpe8, Rpe9, Weight, Workout}

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class Hypertrophy5Day(implicit val workoutHistory: Seq[Workout]) {

  import worksets.Predef._

  private val squatDay = { date: LocalDate =>
    (
      newWorkout(date.toString)
        exercise CompetitionSquat withWorkingSet(Weight(80.0), 10, Rpe6) withWorkingSetRelative(1.0, 10, 3) end()
        exercise CompetitionBench withWorkingSet(Weight(70.0), 10, Rpe6) withWorkingSetRelative(1.0, 10, 3) end()
        exercise StandingPressStandard withWorkingSet(Weight(55.0), 6, Rpe6) withWorkingSetRelative(1.0, 6, 3) end()
        endWorkout()
      )
  }

  private val deadLiftDay = { date: LocalDate =>
    (
      newWorkout(date.toString)
        exercise CompetitionDeadlift withWorkingSet(Weight(110.0), 6, Rpe6) withWorkingSetRelative(1.1, 6, 4) end()
        exercise PendlayRowStandard withWorkingSet(Weight(90.0), 4, Rpe8) withWorkingSetRelative(0.90, 6, 3) end()
        exercise WideGripBench withWorkingSet(Weight(65.0), 10, Rpe7) withWorkingSetRelative(1.0, 10, 3) end()
        endWorkout()
      )
  }

  private val lightDay = { date: LocalDate =>
    (
      newWorkout(date.toString)
        exercise FrontSquatStandard withWorkingSet(Weight(80.0), 10, Rpe8) withWorkingSetRelative(0.92, 10, 3) end()
        exercise BodyWeightPullups withWorkingSet (Weight(80.0), 5, Rpe7) withWorkingSetRelative(1.0, 5, 3) end()
        endWorkout()
      )
  }


  val weeklyProgram: List[(Day, LocalDate => Workout)] = List(
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Wednesday -> lightDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )

  def workoutForNextWeek(startDate: LocalDate = LocalDate.now()): Seq[worksets.Workout] = {
    weeklyProgram.map { case (dayOfWeek, dateToWorkout) =>
      dateToWorkout(dayOfWeek.next(startDate))
    }
  }

}

object Hypertrophy5Day {
  def apply(implicit workoutHistory: Seq[Workout]): Hypertrophy5Day = new Hypertrophy5Day
}