package worksets

import scala.collection.SortedSet

case class Plate(weight: Weight) extends Ordered[Plate]:
  override def compare(that: Plate): Int = this.weight.grams.compareTo(that.weight.grams)

object Plate {

  val `1.25`: Plate = Plate(1.25.kg)
  val `2.5`: Plate = Plate(2.5.kg)
  val `5.0`: Plate = Plate(5.kg)
  val `10.0`: Plate = Plate(10.kg)
  val `20.0`: Plate = Plate(20.kg)

  def weightToPlates(weight: Weight)(implicit availablePlates: SortedSet[Plate]): List[Plate] =
    val remainingWeight = roundHalfUp(weight.grams, 500)
    val plates = availablePlates.toList.foldRight(WeightToPlatesState(List.empty, remainingWeight)) {
      case (plate, state) if plate.weight.grams*2 <= state.remainingWeightGrams =>
        val requiredPlatePairs = state.remainingWeightGrams / (plate.weight.grams*2)
        val usedPlates = List.fill(requiredPlatePairs)(plate)
        WeightToPlatesState(
          state.usedPlates ++ usedPlates,
          state.remainingWeightGrams - requiredPlatePairs*2*plate.weight.grams
        )
      case (_, state) => state
    }
    plates.usedPlates

  def roundToAvailablePlates(weight: Weight)(implicit availablePlates: SortedSet[Plate]): Weight =
    Weight(weightToPlates(weight).map(_.weight.grams).sum * 2)

  private case class WeightToPlatesState(usedPlates: List[Plate], remainingWeightGrams: Int)

  @inline
  private def roundHalfUp(value: Int, step: Int) =
    val half = step/2
    value % step match
      case v if v < half => value - v
      case v if v >= half => value + v
      case _ => value

}