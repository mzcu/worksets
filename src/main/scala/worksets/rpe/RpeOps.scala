package worksets.rpe

import worksets.{Rpe, Rpe10, Rpe6, Rpe7, Rpe8, Rpe9, RpeUndefined, Weight}

import scala.collection.Map

/**
 * Created by on 04-01-20.
 */
@SuppressWarnings(Array("org.wartremover.warts.All"))
object RpeOps {

  val defaultRpe10Chart: List[Double] =
    List(1.0, .955, .922, .892, .863, .837, .811, .786, .762, .739, .707, .680, .653, .626, .599)
  val reps = 1 to 15

  val rpe = Seq(Rpe6, Rpe7, Rpe8, Rpe9, Rpe10)

  val chart: Map[Rpe, Map[Int, Double]] = {
    rpe.reverse.zipWithIndex.map { t: (Rpe, Int) =>
      t._1 -> reps.zip(defaultRpe10Chart.drop(t._2)).toMap
    }.toMap
  }

  def toPct(reps: Int, rpe: Rpe): Double = chart(rpe)(reps)

  def toRpe(reps: Int, pct: Double): Rpe =
    chart.map((tuple: (Rpe, Map[Int, Double])) => {
      val diff = Math.abs(pct - tuple._2(reps))
      (diff, tuple._1)
    }).toList.sortBy(_._1).headOption.map(_._2).getOrElse(RpeUndefined)

  def e1rm(weight: Weight, reps: Int, rpe: Rpe): Weight = {
    val pct = RpeOps.toPct(reps, rpe)
    Weight((weight.grams / pct).toInt)
  }
}
