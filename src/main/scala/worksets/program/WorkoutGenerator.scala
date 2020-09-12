package worksets.program

import java.time.LocalDate

import worksets.WorkoutHistory

/**
 * Created by on 31-07-20.
 */

trait WorkoutGenerator {

  val weeklyProgram: WeeklyProgram

  def generate(startDate: LocalDate): WorkoutHistory = {
    weeklyProgram.map { case (dayOfWeek, workout) =>
      workout.copy(date = dayOfWeek.next(startDate))
    }
  }

  def programName: String = this.getClass.getSimpleName
}
