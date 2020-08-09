package worksets.parser

import worksets.{IntWorksetOps, UnitSpec}

/**
 * Created by on 08-08-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
class WorkoutParserTest extends UnitSpec {

  import WorkoutParser.parseLine

  it should "parse set literal 1" in {
    inside(parseLine("100x1@7")) { case SetLiteral(s) =>
      s.weight should be (100.kg)
      s.reps should be (1)
      s.rpe should be (7.rpe)
    }
  }

  it should "parse set literal 2" in {
    inside(parseLine("100 kg  x 1 @ 7.0")) { case SetLiteral(s) =>
      s.weight should be (100.kg)
      s.reps should be (1)
      s.rpe should be (7.rpe)
    }
  }

  it should "parse set modifier (single mod)" in {
    inside(parseLine("~set rpe+1")) { case WorksetMod(scope, mods) =>
      scope should be theSameInstanceAs SetScope
      mods should contain theSameElementsInOrderAs List(RpeMod(1.0))
    }
  }

  it should "parse set modifier (multiple mods)" in {
    inside(parseLine("~set wei-1 rep+1 rpe-1")) { case WorksetMod(scope, mods) =>
      scope should be theSameInstanceAs SetScope
      mods should contain theSameElementsInOrderAs List(WeightMod(-1.0), RepsMod(1), RpeMod(-1.0))
    }
  }


  it should "parse set modifier (skip mod)" in {
    inside(parseLine("~set -")) { case WorksetMod(scope, mods) =>
      scope should be theSameInstanceAs SetScope
      mods should contain theSameElementsInOrderAs List(SkipMod.value)
    }
  }

}
