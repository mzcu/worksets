package worksets.workouts

import worksets.Predef.{CompetitionDeadlift, CompetitionSquat}
import worksets.support._
import worksets.workouts.Dsl._
import worksets.{UnitSpec, Workout}
import worksets.Ops._

import scala.language.postfixOps


/**
 * Created by on 31-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class WorkoutStatsTest extends UnitSpec:

  given history as Seq[Workout] = Seq.empty
  val testWorkout: Workout = (workout
    exercise CompetitionSquat workset 100.kg x 10 at 8.rpe worksetRelative 100.pct x 10 sets 4
    exercise CompetitionDeadlift workset 110.kg x 6 at 6.rpe worksetRelative 90.pct x 6 sets 4
    )

  it should "calculate correct volume per exercise" in {
    val actualVolumePerExercise = WorkoutStats.volumePerExercise(testWorkout)
    actualVolumePerExercise should contain theSameElementsAs List(
      (CompetitionSquat, 5000 kg),
      (CompetitionDeadlift, 3000 kg)
    )
  }

  it should "calculate top set per exercise" in {
    val actualTopSets = WorkoutStats.topSetPerExercise(testWorkout)
    actualTopSets should contain theSameElementsAs List(
      (CompetitionSquat, worksets.Set(100.kg, 10, 8.rpe)),
      (CompetitionDeadlift, worksets.Set(110.kg, 6, 6.rpe)),
    )
  }

  it should "calculate e1rm per exercise" in {
    val actualE1RMs = WorkoutStats.e1rmPerExercise(testWorkout)
    actualE1RMs.filter(_._1 == CompetitionSquat).head._2.grams should be (147000 +- 500)
    actualE1RMs.filter(_._1 == CompetitionDeadlift).head._2.grams should be (149000 +- 500)
  }


  it should "calculate max intensity per exercise" in {
    val actualMaxIntensities = WorkoutStats.maxIntensityPerExercise(testWorkout)
    actualMaxIntensities.filter(_._1 == CompetitionSquat).head._2 should be (0.68 +- 0.01)
    actualMaxIntensities.filter(_._1 == CompetitionDeadlift).head._2 should be (0.73 +- 0.01)
  }
