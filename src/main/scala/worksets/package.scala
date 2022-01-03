import java.time.LocalDate

import worksets.support.{Monoid, Quantity, combineAll}


package object worksets:

  case class Weight(grams: Int) extends AnyVal

  object Weight:
    val zero: Weight = Weight(0)

  given weightQuantity: Quantity[Weight] with
    extension (x: Weight) def +(y: Weight) = Weight(x.grams + y.grams)
    extension (x: Weight) def *(y: Double) = Weight((x.grams * y).toInt)
    def compare(x: Weight, y: Weight) = x.grams.compareTo(y.grams)
  

  given weightMonoid: Monoid[Weight] with
    override def empty: Weight = Weight.zero
    extension (x: Weight) def combine(y: Weight) = x + y
  
  sealed trait Rpe
  case object RpeUndefined extends Rpe
  case class RpeVal private (value: Double) extends Rpe
  object RpeVal:
    def apply(value: Double): RpeVal =
      require(value >= 5.0 && value <= 10.0, "RPE makes sense for values between 5.0 and 10.0")
      new RpeVal(value)
  case class Set(weight: Weight, reps: Int, rpe: Rpe):
    def *(count: Int): List[Set] = List.fill(count)(this)
    def volume: Weight = weight * reps
  object Set:
    import worksets.Ops._
    val empty: Set = Set(0.kg, 0, RpeUndefined)
  case class WorkSet(exercise: ExerciseWithMods, target: Set, actual: Set, ord: Int = Int.MinValue, completed: Boolean = false):
    import worksets.Ops._
    def volume: Weight = actual.volume
    def difficulty: Double = actual.rpe.asDouble

  case class Exercise(name: String)
  case class ExerciseWithMods(exercise: Exercise, barType: BarType, mods: Mods)

  sealed trait Mods:
    val tempo: Option[TempoMod] = None
    val stance: Option[StanceMod] = None
    val grip: Option[GripMod] = None
    val kit: Option[KitMod] = None

  case object NoMods extends Mods

  case class WithMods(override val tempo: Option[TempoMod] = None,
                      override val stance: Option[StanceMod] = None,
                      override val grip: Option[GripMod] = None,
                      override val kit: Option[KitMod] = None) extends Mods

  case class Workout(date: LocalDate, sets: List[WorkSet]):
    def volume: Weight = sets.map(_.volume).combineAll()
    def difficulty: Double = sets.map(_.difficulty).sum / sets.size

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
  case object Deficit extends StanceMod

  sealed trait GripMod

  case object VeryCloseGrip extends GripMod

  case object CloseGrip extends GripMod

  case object WideGrip extends GripMod

  case object VeryWideGrip extends GripMod
  

  trait DoubleOps:
    extension (value: Double)
      def kg: Weight = Weight((value*1000).toInt)
      def rpe: Rpe = RpeVal(value)

  trait IntOps:
    extension (value: Int)
      def kg: Weight = Weight(value*1000)
      def reps: Int = value
      def rpe: Rpe = RpeVal(value.toDouble)

  trait RpeOps:
    extension(rpe: Rpe) def asDouble: Double = rpe match
      case RpeVal(v) => v
      case _ => 0d

  object Ops extends DoubleOps, IntOps, RpeOps
