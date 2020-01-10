package worksets

import scala.collection.SortedSet
import scala.collection.mutable.ListBuffer

case class Plate(weight: Weight) extends Ordered[Plate] {
  override def compare(that: Plate): Int = this.weight.grams.compareTo(that.weight.grams)
}

@SuppressWarnings(Array("org.wartremover.warts.All"))
object Plate {

  val `1.25`: Plate = Plate(Weight(1250))
  val `2.5`: Plate = Plate(Weight(2500))
  val `5.0`: Plate = Plate(Weight(5000))
  val `10.0`: Plate = Plate(Weight(10000))
  val `20.0`: Plate = Plate(Weight(20000))

  def weightToPlates(weight: Weight)(implicit availablePlates: SortedSet[Plate]): List[Plate] = {
    var remainingWeight = weight.grams
    val plates = availablePlates.toList.reverse
    val result = ListBuffer[Plate]()
    plates.foreach { case p@Plate(Weight(plateWeight)) =>
      while (plateWeight*2 <= remainingWeight) {
        remainingWeight -= plateWeight*2
        result += p
      }
    }
    result.toList
  }

  def roundToAvailablePlates(weight: Weight)(implicit availablePlates: SortedSet[Plate]): Weight =
    Weight(weightToPlates(weight).map(_.weight.grams).sum * 2)

}