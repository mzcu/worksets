import java.time.LocalDate

import scala.reflect.ClassTag


package object worksets {

  case class Weight(grams: Int) extends AnyVal

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
  object Set {
    val empty: Set = Set(0.kg, 0, RpeUndefined)
  }
  case class WorkSet(exercise: ExerciseWithMods, target: Set, actual: Set, ord: Int = Int.MinValue, completed: Boolean = false)

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


  implicit class DoubleOps(value: Double) {
    def kg: Weight = Weight((value*1000).toInt)
  }

  implicit class IntOps(value: Int) {
    def kg: Weight = Weight(value*1000)
    def reps: Int = value
    def rpe: RpeVal = RpeVal(value.toDouble)
    def pct: Percent = Percent(value)
  }

  case class Percent(value: Int) extends AnyVal {
    def asRatio: Double = value/100.0
  }

  // type-classes

  trait Quantity[T] {
    def *(multiplier: Double): T
    def +(summand: T): T
    def *[X: ClassTag](percent: Percent): T = this.*(percent.asRatio)
  }

  implicit class WeightIsAQuantity(val value: Weight) extends Quantity[Weight] {
    override def +(that: Weight): Weight = Weight(this.value.grams + that.grams)
    override def *(that: Double): Weight = Weight((this.value.grams * that).toInt)
  }

}
