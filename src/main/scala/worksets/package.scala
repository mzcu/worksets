import java.time.LocalDate


package object worksets {

  case class Weight(grams: Int)

  object Weight {
    def apply(kilos: Double): Weight = Weight((kilos * 1000).toInt)
  }

  sealed trait Rpe
  case object RpeUndefined extends Rpe
  case class RpeVal(value: Double) extends Rpe
  object Rpe6 extends RpeVal(6.0)
  object Rpe7 extends RpeVal(7.0)
  object Rpe8 extends RpeVal(8.0)
  object Rpe9 extends RpeVal(9.0)
  object Rpe10 extends RpeVal(10.0)

  case class Set(weight: Weight, reps: Int, rpe: Rpe) {
    def *(count: Int): List[Set] = List.fill(count)(this)
  }
  case class WorkSet(exercise: ExerciseWithMods, target: Set, actual: Set, ord: Int = Int.MinValue)

  case class Exercise(name: String)
  case class ExerciseWithMods(exercise: Exercise, barType: BarType, mods: Mods)

  sealed trait Mods {
    val tempo: Option[TempoMod] = None
    val stance: Option[StanceMod] = None
    val grip: Option[GripMod] = None
    val kit: Option[KitMod] = None
  }

  case object NoMods extends Mods

  case class WithMods(override val tempo: Option[TempoMod] = None,
                      override val stance: Option[StanceMod] = None,
                      override val grip: Option[GripMod] = None,
                      override val kit: Option[KitMod] = None) extends Mods

  case class Workout(date: LocalDate, sets: List[WorkSet])

  sealed trait BarType

  case object Barbell extends BarType

  case object TrapBar extends BarType

  sealed trait TempoMod

  case object Tempo extends TempoMod

  case object TouchAndGo extends TempoMod

  case class Pause(ct: Int) extends TempoMod

  case class CustomTempoMod(eccentric: Int, isometric: Int, concentric: Int) extends TempoMod

  sealed trait KitMod

  case object Belt extends KitMod

  sealed trait StanceMod

  case object FeetUp extends StanceMod

  sealed trait GripMod

  case object VeryCloseGrip extends GripMod

  case object CloseGrip extends GripMod

  case object WideGrip extends GripMod

  case object VeryWideGrip extends GripMod
}
