package worksets.program

import java.time.LocalDate

import worksets.calendar._
import worksets.workout.WorkoutBuilder.newWorkout
import worksets.{Rpe8, Rpe9, Weight, Workout}

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class Hypertrophy5Day(implicit val workoutHistory: Seq[Workout]) {

  import worksets.Predef._

  private val squatDay = { date: LocalDate =>
    (
      newWorkout(date.toString)
        exercise CompetitionSquat withWorkingSet(Weight(120.0), 1, Rpe8) withWorkingSetRelative(.77, 5, 2) end()
        exercise CompetitionBench withWorkingSet(Weight(100.0), 1, Rpe8) withWorkingSetRelative(.87, 4, 3) end()
        endWorkout()
      )
  }

  private val deadLiftDay = { date: LocalDate =>
    (
      newWorkout(date.toString)
        exercise CompetitionDeadlift withWorkingSet(Weight(127.5), 1, Rpe8) withWorkingSetRelative(.82, 5, 2) end()
        exercise PendlayRowStandard withWorkingSet(Weight(80.0), 6, Rpe8) withWorkingSetRelative(.8, 5, 2) end()
        exercise PausedBench3ct withWorkingSet(Weight(80.0), 1, Rpe8) withWorkingSet(Weight(80.0), 6, Rpe9) loadDrop 1 end()
        endWorkout()
      )
  }

  private val lightDay = { date: LocalDate =>
    (
      newWorkout(date.toString)
        exercise FrontSquatStandard withWorkingSet(Weight(80.0), 6, Rpe8) end()
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