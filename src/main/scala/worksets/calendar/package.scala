package worksets

import java.time.temporal.TemporalAdjusters
import java.time.{DayOfWeek, LocalDate}

/**
 * Created by on 26-07-20.
 */
package object calendar {

  sealed class Day {

    def next(afterDate: LocalDate): LocalDate = {
      val dayOfWeek = toJavaDayOfWeek
      afterDate `with` TemporalAdjusters.nextOrSame(dayOfWeek)
    }

    private def toJavaDayOfWeek: DayOfWeek = {
      DayOfWeek.valueOf(this.toString.toUpperCase())
    }
  }

  case object Monday extends Day

  case object Tuesday extends Day

  case object Wednesday extends Day

  case object Thursday extends Day

  case object Friday extends Day

  case object Saturday extends Day

  case object Sunday extends Day


}
