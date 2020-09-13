package worksets.program

import worksets._
import worksets.Predef._
import worksets.calendar._
import worksets.support.IntPercentOps
import worksets.workouts.Dsl._

/**
 * Created by on 22-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object Hypertrophy5Day extends WorkoutGenerator {

  private val squatDay: Workout = (
      workout
        exercise CompetitionSquat workset 80.kg x 10 at 6.rpe repeat 3
        exercise CompetitionBench workset 70.kg x 10 at 6.rpe repeat 3
        exercise StandingPressStandard workset 55.kg x 6 at 6.rpe repeat 3
      )


  private val deadLiftDay: Workout = (
      workout
        exercise CompetitionDeadlift workset 110.kg x 6 at 6.rpe worksetRelative 110.pct x 6 sets 4
        exercise PendlayRowStandard workset 90.0.kg x 4 at 8.rpe worksetRelative 90.pct x 6 sets 3
        exercise WideGripBench workset 65.kg x 10 at 7.rpe repeat 3
      )

  private val lightDay: Workout = (
    workout
        exercise FrontSquatStandard workset 80.kg x 10 at 8.rpe worksetRelative 92.pct x 10 sets 3
        exercise BodyWeightPullups workset 80.kg x 5 at 7.rpe repeat 3
    )

  val weeklyProgram: WeeklyProgram = List(
    Monday -> squatDay,
    Tuesday -> deadLiftDay,
    Wednesday -> lightDay,
    Thursday -> squatDay,
    Friday -> deadLiftDay
  )
}