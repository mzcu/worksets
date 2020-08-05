package worksets.workouts

import java.time.LocalDate
import scala.language.postfixOps

import worksets.Predef.{CompetitionSquat, CompetitionDeadlift}
import worksets.{IntWorksetOps, UnitSpec, Workout}
import Dsl._
import worksets.support._


/**
 * Created by on 31-07-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class WorkoutStatsTest extends UnitSpec {

  private val day = LocalDate.now()
  implicit private val history: Seq[Workout] = Seq.empty
  val testWorkout: Workout = (workout on day
    exercise CompetitionSquat workset 100.kg x 10 at 8.rpe worksetRelative 100.pct x 10 sets 4
    exercise CompetitionDeadlift workset 100.kg x 6 at 7.rpe worksetRelative 100.pct x 6 sets 4
    )

  it should "calculate correct volume per exercise" in {
    val actualVolumePerExercise = WorkoutStats.volumePerExercise(testWorkout)
    actualVolumePerExercise should contain theSameElementsAs List(
      (CompetitionSquat, 5000 kg),
      (CompetitionDeadlift, 3000 kg)
    )

  }


}
