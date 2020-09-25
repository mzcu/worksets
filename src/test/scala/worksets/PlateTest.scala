package worksets

import scala.collection.SortedSet

/**
 * Created by on 05-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class PlateTest extends UnitSpec:

  implicit val availablePlates: SortedSet[Plate] = SortedSet(Plate.`1.25`, Plate.`2.5`, Plate.`5.0`, Plate.`10.0`, Plate.`20.0`)

  it should "select correct plates 1" in {
    Plate.weightToPlates(77.5.kg) shouldEqual availablePlates.toList.reverse
  }

  it should "round down to available plates" in {
    Plate.roundToAvailablePlates(79.kg) shouldEqual 77.5.kg
    Plate.roundToAvailablePlates(80.kg) shouldEqual 80.kg
    Plate.roundToAvailablePlates(81.9.kg) shouldEqual 80.kg
  }

