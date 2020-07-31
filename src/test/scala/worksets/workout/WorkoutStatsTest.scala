package worksets.workout

import java.time.LocalDate

import worksets.Predef.{CompetitionDeadlift, CompetitionSquat}
import worksets.Syntax._
import worksets.workout.WorkoutBuilder.workout
import worksets.{UnitSpec, Workout}
import WorkoutDsl._


/**
 * Created by on 31-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class WorkoutStatsTest extends UnitSpec {

  private val day = LocalDate.now()
  implicit private val history: Seq[Workout] = Seq.empty
  val testWorkout: Workout = (workout on day
    exercise CompetitionSquat workset 100.kg x 10 at 8.rpe worksetRelative 1.0 x 10 sets 4
    exercise CompetitionDeadlift workset 100.kg x 6 at 7.rpe worksetRelative 1.0 x 6 sets 4
    )

  it should "calculate correct volume" in {
    val actualVolume = WorkoutStats.volume(testWorkout)
    actualVolume shouldBe (100*10*5 + 100*6*5).kg
  }

  it should "calculate correct intensity" in {
    val actualIntensity = WorkoutStats.intensity(testWorkout)
    actualIntensity shouldBe (7.5 +- .01)
  }

}
