package worksets

import scala.collection.SortedSet

/**
 * Created by on 05-01-20.
 */
object WorksetOps {

  private implicit val availablePlates: SortedSet[Plate] =
    SortedSet(Plate.`1.25`, Plate.`2.5`, Plate.`5.0`, Plate.`10.0`, Plate.`20.0`)


  def createSet(weight: Weight, reps: Int, rpe: Rpe): Set = {
    val rounded = Plate.roundToAvailablePlates(weight)
    Set(rounded, reps, rpe)
  }

  def roundSet(set: Set): Set = {
    val rounded = Plate.roundToAvailablePlates(set.weight)
    set.copy(weight = rounded)
  }
}
