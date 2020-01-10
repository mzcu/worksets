package worksets

import scala.collection.SortedSet

/**
 * Created by on 05-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class PlateTest extends UnitSpec {

  implicit val availablePlates = SortedSet(Plate.`1.25`, Plate.`2.5`, Plate.`5.0`, Plate.`10.0`, Plate.`20.0`)

  it should "select correct plates 1" in {
    Plate.weightToPlates(Weight(77.5)) shouldEqual availablePlates.toList.reverse
  }

  it should "round down to available plates" in {
    Plate.roundToAvailablePlates(Weight(79.0)) shouldEqual Weight(77.5)
    Plate.roundToAvailablePlates(Weight(80.0)) shouldEqual Weight(80.0)
    Plate.roundToAvailablePlates(Weight(81.9)) shouldEqual Weight(80.0)
  }

}
