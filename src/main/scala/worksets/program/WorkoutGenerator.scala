package worksets.program

import java.time.LocalDate

import worksets.Workout
import worksets.calendar.Day

/**
 * Created by on 31-07-20.
 */

trait WorkoutGenerator {

  type WeeklyProgram = List[(Day, LocalDate => Workout)]

  val weeklyProgram: WeeklyProgram
  def generate(startDate: LocalDate = LocalDate.now()): Seq[worksets.Workout] = {
    weeklyProgram.map { case (dayOfWeek, dateToWorkout) =>
      dateToWorkout(dayOfWeek.next(startDate))
    }
  }
}
