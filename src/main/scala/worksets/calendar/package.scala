package worksets

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, SignStyle}
import java.time.temporal.{IsoFields, TemporalAdjusters}
import java.time.{DayOfWeek, LocalDate}

/**
 * Created by on 26-07-20.
 */
package object calendar {

  val YearWeekFormatter: DateTimeFormatter = new DateTimeFormatterBuilder()
    .parseCaseInsensitive.appendValue(IsoFields.WEEK_BASED_YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
    .appendLiteral("-W")
    .appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR, 2)
    .toFormatter

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
