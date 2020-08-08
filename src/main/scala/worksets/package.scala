import java.time.LocalDate

import worksets.support.{ListMonoid, Monoid, Quantity}


package object worksets {

  case class Weight(grams: Int) extends AnyVal

  object Weight {
    val zero: Weight = Weight(0)
    def apply(kilos: Double): Weight = Weight((kilos * 1000).toInt)
  }

  implicit class WeightIsAQuantity(val value: Weight) extends Quantity[Weight] {
    override def +(that: Weight): Weight = Weight(this.value.grams + that.grams)
    override def *(that: Double): Weight = Weight((this.value.grams * that).toInt)
  }

  implicit object WeightIsAMonoid extends Monoid[Weight] {
    override def empty: Weight = Weight.zero
    override def combine(first: Weight, second: Weight): Weight = first + second
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
    def volume: Weight = weight * reps
  }
  object Set {
    val empty: Set = Set(0.kg, 0, RpeUndefined)
  }
  case class WorkSet(exercise: ExerciseWithMods, target: Set, actual: Set, ord: Int = Int.MinValue, completed: Boolean = false) {
    def volume: Weight = actual.volume
    def intensity: Double = actual.rpe.asDouble
  }

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

  case class Workout(date: LocalDate, sets: List[WorkSet]) {
    def volume: Weight = sets.map(_.volume).combineAll
    def intensity: Double = sets.map(_.intensity).sum / sets.size
  }

  type WorkoutHistory = Seq[Workout]

  sealed trait BarType

  case object Barbell extends BarType

  case object TrapBar extends BarType

  case object NoBar extends BarType

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

  implicit class RpeOps(val rpe: Rpe) {
    def asDouble: Double = rpe match {
      case RpeVal(v) => v
      case _ => 0d
    }
  }

  implicit class DoubleWorksetOps(value: Double) {
    def kg: Weight = Weight((value*1000).toInt)
    def rpe: Rpe = value match {
      case 0d | Double.NaN => RpeUndefined
      case d => RpeVal(d)
    }
  }

  implicit class IntWorksetOps(value: Int) {
    def kg: Weight = Weight(value*1000)
    def reps: Int = value
    def rpe: RpeVal = RpeVal(value.toDouble)
  }


}
