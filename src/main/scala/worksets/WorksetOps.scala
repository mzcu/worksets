package worksets

import scala.collection.SortedSet
import worksets.Predef.{availablePlates}

/**
 * Created by on 05-01-20.
 */
object WorksetOps:

  def createSet(weight: Weight, reps: Int, rpe: Rpe): Set =
    val rounded = Plate.roundToAvailablePlates(weight)
    Set(rounded, reps, rpe)

  def roundSet(set: Set): Set =
    val rounded = Plate.roundToAvailablePlates(set.weight)
    set.copy(weight = rounded)
