package worksets.program

import java.time.LocalDate

import worksets.WorkoutHistory
import worksets.repository.ObjectStore

/**
 * Created by on 31-07-20.
 */

trait WorkoutGenerator {

  implicit val workoutHistory: WorkoutHistory = ObjectStore.load()
  def generate(startDate: LocalDate): WorkoutHistory = {
    weeklyProgram.map { case (dayOfWeek, workout) =>
      workout.copy(date = dayOfWeek.next(startDate))
    }
  }
  def programName: String = this.getClass.getSimpleName

  val weeklyProgram: WeeklyProgram
}
