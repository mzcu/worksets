package worksets

import worksets.Predef.{CompetitionDeadlift, CompetitionSquat}
import worksets.support.IntPercentOps
import worksets.workouts.Dsl.workout

/**
 * Created by on 05-08-20.
 */
class WorkoutTest extends UnitSpec:

  implicit private val history: Seq[Workout] = Seq.empty
  val underTest: Workout = (workout
    exercise CompetitionSquat workset 100.kg x 10 at 8.rpe worksetRelative 100.pct x 10 sets 4
    exercise CompetitionDeadlift workset 100.kg x 6 at 7.rpe worksetRelative 100.pct x 6 sets 4
    )


  it should "calculate correct volume" in {
    val actualVolume = underTest.volume
    actualVolume shouldBe (100*10*5 + 100*6*5).kg
  }

  it should "calculate correct intensity" in {
    val actualIntensity = underTest.intensity
    actualIntensity shouldBe (7.5 +- .01)
  }

