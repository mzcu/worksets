package worksets.parser

import worksets.{DoubleWorksetOps, IntWorksetOps, WeightIsAQuantity}

import scala.util.parsing.combinator.RegexParsers

@SuppressWarnings(Array("org.wartremover.warts.All"))
object WorkoutParser extends RegexParsers {

  def int: Parser[Int] =
    """(\d+)""".r ^^ {
      _.toInt
    }

  def double: Parser[Double] =
    """([+-])?[0-9]+(\.[0-9]+)?""".r ^^ {
      _.toDouble
    }

  def rpeMod: Parser[RpeMod] = "rpe" ~ double ^^ { case _ ~ double => RpeMod(double) }

  def weightMod: Parser[WeightMod] = "wei" ~ double ^^ { case _ ~ double => WeightMod(double) }

  def repsMod: Parser[RepsMod] = "rep" ~ double ^^ { case _ ~ double => RepsMod(double.toInt) }

  def qualifier: Parser[ModifierScope] = "~" ~ "(exe|set)".r ^^ { case _ ~ scope =>
    scope match {
      case "exe" => ExerciseScope
      case "set" => SetScope
    }
  }

  def workset: Parser[SetLiteral] = (int <~ "kg".?) ~ ("x" ~> int) ~ ("@" ~> double) ^^ { case w ~ reps ~ intensity =>
    SetLiteral(worksets.Set(w.kg, reps, intensity.rpe))
  }

  def worksetMod: Parser[WorksetMod] = (qualifier ~ (weightMod.? ~ repsMod.? ~ rpeMod.?)) ^^ { mod =>
    val scope = mod._1
    val rpe = mod._2._2.toList
    val rep = mod._2._1._2.toList
    val wei = mod._2._1._1.toList
    WorksetMod(scope, wei ::: rep ::: rpe)
  }

  def parseLine(input: String): WorkoutParserResult = parse(workset | worksetMod, input) match {
    case Success(p: WorkoutParserResult, _) => p
    case NoSuccess(err, _) => WorkoutParserError(err)
    case _ => WorkoutParserError("unknown")
  }

}


sealed trait SetModifier {
  def modify(set: worksets.Set): worksets.Set
}
case class RpeMod(value: Double) extends SetModifier {
  override def modify(set: worksets.Set): worksets.Set = set.copy(rpe = (set.rpe.asDouble + value).rpe)
}
case class WeightMod(value: Double) extends SetModifier {
  override def modify(set: worksets.Set): worksets.Set = set.copy(weight = set.weight + value.kg)
}
case class RepsMod(value: Int) extends SetModifier {
  override def modify(set: worksets.Set): worksets.Set = set.copy(reps = set.reps + value)
}

sealed trait ModifierScope
case object ExerciseScope extends ModifierScope
case object SetScope extends ModifierScope

sealed trait WorkoutParserResult
case class WorksetMod(scope: ModifierScope, mods: List[SetModifier]) extends WorkoutParserResult
case class SetLiteral(set: worksets.Set) extends WorkoutParserResult
case class WorkoutParserError(msg: String) extends WorkoutParserResult