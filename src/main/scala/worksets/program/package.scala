package worksets

import java.time.LocalDate

import worksets.calendar.Day

/**
 * Created by on 01-08-20.
 */
package object program {
  type WeeklyProgram = List[(Day, Workout)]
}
