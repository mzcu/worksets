package worksets.interactive

import worksets.program.Hypertrophy5Day
import worksets.repository.ObjectStore
import worksets.workout.WorkoutStats

/**
 * Created by on 03-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object ProgramNextWeek extends App {
  val workoutHistory: Seq[worksets.Workout] = ObjectStore.load()
  val week = Hypertrophy5Day(workoutHistory).workoutForNextWeek()
  ObjectStore.store(workoutHistory ++ week)

  import worksets.Show._
  import worksets.ConsoleView._

  week.foreach { day =>
    import WorkoutStats._
    println(
      s"""
         |Date: \t${day.date.show}
         |Volume: \t${volume(day).show}
         |Avg RPE: \t${intensity(day).formatted("%.1f")}
         |
         |${day.show}
         |
         |""".stripMargin)
  }

}
