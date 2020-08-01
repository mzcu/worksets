package worksets

/**
 * Created by on 03-01-20.
 */
object Main {
  @SuppressWarnings(Array("org.wartremover.warts.All"))
  def main(args: Array[String]): Unit = {
    ammonite.Main(welcomeBanner = Some("Worksets 0.1"), predefCode =
      """
        |repl.prompt.bind { "worksets# " }
        |
        |
        |def programNextWeek = worksets.cli.ProgramNextWeek.run
        |def enterResults = worksets.cli.EnterResults.interact
        |val report = worksets.cli.ShowReport
        |
        |""".stripMargin)
      .run()
  }
}
