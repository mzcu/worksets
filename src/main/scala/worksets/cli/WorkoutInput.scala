package worksets.cli

import org.jline.reader.{LineReader, LineReaderBuilder}
import worksets.parser.WorkoutParser

/**
 * Created by on 07-08-20.
 */
object WorkoutInput {
  val parser: WorkoutParser.type = WorkoutParser
  val reader: LineReader = LineReaderBuilder.builder().appName("worksets").build()
}
