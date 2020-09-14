package worksets

import worksets.calendar.Day

/**
 * Created by on 01-08-20.
 */
package object program {
  type WeeklyProgram = List[(Day, Workout)]
}
