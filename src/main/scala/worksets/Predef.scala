package worksets

import scala.collection.SortedSet

/**
 * Created by on 04-01-20.
 */
object Predef:


  given availablePlates: SortedSet[Plate] =
    SortedSet(Plate.`1.25`, Plate.`2.5`, Plate.`5.0`, Plate.`10.0`, Plate.`20.0`)

  // Exercises
  val Bench: Exercise = Exercise("Bench Press")
  val Deadlift: Exercise = Exercise("Deadlift")
  val Squat: Exercise = Exercise("Squat")
  val PendlayRow: Exercise = Exercise("Pendlay Row")
  val FrontSquat: Exercise = Exercise("Front Squat")
  val StandingPress: Exercise = Exercise("Standing Press")
  val PullUps: Exercise = Exercise("Pull-ups")

  // Exercises with mods

  // Bench press
  val CompetitionBench: ExerciseWithMods = ExerciseWithMods(Bench, Barbell, NoMods)
  val PausedBench3ct: ExerciseWithMods = CompetitionBench.copy(mods = WithMods(tempo = Some(Pause(3))))
  val TouchAndGoBench: ExerciseWithMods = CompetitionBench.copy(mods = WithMods(tempo = Some(TouchAndGo)))
  val FeetUpBench: ExerciseWithMods = CompetitionBench.copy(mods = WithMods(stance = Some(FeetUp)))
  val WideGripBench: ExerciseWithMods = CompetitionBench.copy(mods = WithMods(grip = Some(WideGrip)))

  // Squat
  val CompetitionSquat: ExerciseWithMods = ExerciseWithMods(Squat, Barbell, mods = WithMods(kit = Some(Belt)))
  val TempoSquat300: ExerciseWithMods = CompetitionSquat.copy(mods = WithMods(tempo = Some(CustomTempoMod(3, 0, 0))))

  // Deadlift
  val CompetitionDeadlift: ExerciseWithMods = ExerciseWithMods(Deadlift, Barbell, mods = WithMods(kit = Some(Belt)))

  // Front squat
  val FrontSquatStandard: ExerciseWithMods = ExerciseWithMods(FrontSquat, Barbell, NoMods)

  // Row
  val PendlayRowStandard: ExerciseWithMods = ExerciseWithMods(PendlayRow, Barbell, NoMods)

  // Press
  val StandingPressStandard: ExerciseWithMods = ExerciseWithMods(StandingPress, Barbell, NoMods)

  // Pullups
  val BodyWeightPullups: ExerciseWithMods = ExerciseWithMods(PullUps, NoBar, NoMods)
