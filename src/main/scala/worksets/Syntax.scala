package worksets

/**
 * Created by on 30-07-20.
 */
object Syntax {
  implicit class DoubleOps(value: Double) {
    def kg(): Weight = Weight((value*1000).toInt)
  }

  implicit class IntOps(value: Int) {
    def kg: Weight = Weight(value*1000)
    def reps: Int = value
    def rpe: RpeVal = RpeVal(value.toDouble)
  }
}
