package worksets.workouts

import worksets._
import workouts.Dsl._
import worksets.Predef._

/**
 * Created by on 13-09-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.TraversableOps"))
class DslTest extends UnitSpec {

  private def buildWorkout: Workout = {
    implicit val history: WorkoutHistory = Seq.empty
    workout exercise CompetitionBench workset 80.kg x 8 at 7.5.rpe repeat 3
  }

  "worksetByE1RM" should "build based on workout history" in {
      implicit val history: WorkoutHistory = Seq(buildWorkout)
      val generatedWorkout: Workout = workout exercise CompetitionBench worksetByE1RM 7.5.rpe x 8 repeat 3
      val expectedSet = history.head.sets.head.target
      val targetSet = generatedWorkout.sets.head.target
      targetSet shouldEqual expectedSet
  }

}