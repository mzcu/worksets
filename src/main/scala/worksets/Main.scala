package worksets

/**
 * Created by on 10-10-20.
 */
object Main:
  
  def usage =
    """
      | Usage: worksets <command>
      | 
      | <command>
      |   - report
      |   - enterResults
      |   - programNextWeek
      |""".stripMargin
  
  
  def exit(msg: String) =
    Console.err.println(msg)
    println(usage)
    sys.exit(1)
  
  def parseArgs(state: ArgParseState): ArgParseState = state.args match {
    case "report" :: tail => parseArgs(ArgParseState(state.parsed + ("report" -> ""), tail))
    case "enterResults" :: tail => parseArgs(ArgParseState(state.parsed + ("enterResults" -> ""), tail))
    case "programNextWeek" :: tail => parseArgs(ArgParseState(state.parsed + ("programNextWeek" -> ""), tail))
    case _ => state
  }
    
  def main(args: Array[String]): Unit =
    val processed = parseArgs(ArgParseState(Map.empty, args.toList))
    if processed.args.nonEmpty then exit(s"Unrecognized arguments: ${processed.args.mkString(",")}")
    val command = processed.parsed.keys.toList.headOption.getOrElse("report")
    command match {
      case "report" => worksets.cli.ShowReport.main(Array.empty)
      case "enterResults" => worksets.cli.EnterResults.main(Array.empty)
      case "programNextWeek" => worksets.cli.ProgramNextWeek.main(Array.empty)
      case unrecognized => exit(s"Unrecognized command ${unrecognized}")
    }


case class ArgParseState(parsed: Map[String, String], args: List[ String])