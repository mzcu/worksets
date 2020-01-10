package worksets

import worksets.workout.WorkoutBuilder._
import worksets.workout.WorkoutStats

/**
 * Created by on 03-01-20.
 */
object Main extends App {

  import ConsoleView._
  import Predef._
  import Show._


  val day1 = { date: String =>
    (
      newWorkout(date)
        exercise CompetitionSquat withWorkingSet(Weight(120.0), 1, Rpe8) withWorkingSetRelative(.77, 5, 2) end()
        exercise CompetitionBench withWorkingSet(Weight(100.0), 1, Rpe8) withWorkingSetRelative(.87, 4, 3) end()
        exercise FeetUpBench withWorkingSetsByRpe(weight = Weight(70.0), reps = 5, rpe = 6 to 8) loadDrop 1 end()
        endWorkout()
      )
  }

  val day2 = { date: String =>
    (
    newWorkout(date)
      exercise CompetitionDeadlift withWorkingSet(Weight(127.5), 1, Rpe8) withWorkingSetRelative(.82, 5, 2) end()
      exercise PausedBench3ct withWorkingSet(Weight(80.0), 1, Rpe8) withWorkingSet(Weight(80.0), 6, Rpe9) loadDrop 1 end()
      exercise TempoSquat300 withWorkingSetsByRpe(Weight(80.0), 3, 6 to 8) loadDrop 1 end()
      endWorkout()
    )
  }


  val week = Seq(day1("2020-01-06"), day2("2020-01-07"), day1("2020-01-09"), day2("2020-01-10"))

  println("\n\nWorkout sheet")

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
