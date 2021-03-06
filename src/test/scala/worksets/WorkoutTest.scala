package worksets

import worksets.Predef.{CompetitionDeadlift, CompetitionSquat}
import worksets.support.pct
import worksets.workouts.Dsl.workout
import worksets.Ops._

/**
 * Created by on 05-08-20.
 */
class WorkoutTest extends UnitSpec:

  given history: Seq[Workout] = Seq.empty
  val underTest: Workout = (workout
    exercise CompetitionSquat workset 100.kg x 10 at 8.rpe worksetRelative 100.pct x 10 sets 4
    exercise CompetitionDeadlift workset 100.kg x 6 at 7.rpe worksetRelative 100.pct x 6 sets 4
    )


  it should "calculate correct volume" in {
    val actualVolume = underTest.volume
    actualVolume shouldBe (100*10*5 + 100*6*5).kg
  }

  it should "calculate correct difficulty" in {
    val actualIntensity = underTest.difficulty
    actualIntensity shouldBe (7.5 +- .01)
  }

