package worksets.workouts

import worksets._
import worksets.Ops._
import workouts.Dsl._
import worksets.Predef._

/**
 * Created by on 13-09-20.
 */
class DslTest extends UnitSpec:

  private def buildWorkout: Workout =
    given history: WorkoutHistory = Seq.empty
    workout exercise CompetitionBench workset 80.kg x 8 at 7.5.rpe repeat 3

  "worksetByE1RM" should "build based on workout history" in {
      given history: WorkoutHistory = Seq(buildWorkout)
      val generatedWorkout: Workout = workout exercise CompetitionBench worksetByE1RM 7.5.rpe x 8 repeat 3
      val expectedSet = history.head.sets.head.target
      val targetSet = generatedWorkout.sets.head.target
      targetSet shouldEqual expectedSet
  }

  "<weight> x AddOne at <rpe> builder" should "add a single rep to previous workout" in {
    given history: WorkoutHistory = Seq(buildWorkout)
    val generatedWorkout: Workout = workout exercise CompetitionBench workset 80.kg x DynamicReps.AddOne at 7.5.rpe repeat 3
    val expectedSet = history.head.sets.head.target
    val targetSet = generatedWorkout.sets.head.target
    targetSet.reps shouldEqual expectedSet.reps + 1
  }
