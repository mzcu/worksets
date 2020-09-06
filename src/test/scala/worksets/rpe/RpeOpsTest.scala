package worksets.rpe

import org.scalactic.TolerantNumerics
import worksets.{DoubleWorksetOps, IntWorksetOps, Rpe10, RpeVal, UnitSpec, Weight}

@SuppressWarnings(Array("org.wartremover.warts.All"))
class RpeOpsTest extends UnitSpec {

  implicit val doubleEquality = TolerantNumerics.tolerantDoubleEquality(0.01)

  it should "calculate 1RM" in {
    RpeOps.toRpe(1, 1.0) shouldEqual Rpe10
    RpeOps.toPct(1, Rpe10) shouldEqual 1.0
  }

  it should "calculate 4RM" in {
    RpeOps.toRpe(4, 1.0) shouldEqual RpeVal(10)
    RpeOps.toRpe(4, .95) shouldEqual RpeVal(10)
    RpeOps.toRpe(4, .87) shouldEqual RpeVal(9.5)
    RpeOps.toPct(4, Rpe10) shouldEqual .892
  }


  it should "calculate 8RM" in {
    val expectedValues = List(
      (10.rpe, .786),
      (9.5.rpe, .774),
      (9.rpe, .762),
      (8.5.rpe, .751),
      (8.rpe, .739),
      (7.5.rpe, .723),
      (7.rpe, .707),
      (6.5.rpe, .693),
      (6.rpe, .680),
    )
    expectedValues.foreach { case (rpe, pct) =>
      RpeOps.toPct(8, rpe) shouldEqual pct
      RpeOps.toRpe(8, pct) shouldEqual rpe
    }

  }

  it should "calculate E1RM" in {
    RpeOps.e1rm(Weight(75.0), 10, RpeVal(7)).grams should be (115000 +- 1000)
    RpeOps.e1rm(Weight(105.0), 4, RpeVal(8)).grams should be (125000 +- 1000)
    RpeOps.e1rm(Weight(115.0), 1, RpeVal(6)).grams should be (133000 +- 1000)
  }
}
