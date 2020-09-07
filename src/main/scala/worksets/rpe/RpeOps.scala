package worksets.rpe

import worksets.{DoubleWorksetOps, Rpe, RpeVal, Weight}

import scala.math.BigDecimal.double2bigDecimal

/**
 * Coefficients are approximated from Mike Tuscherer's RPE table using following numpy code:
 *
 * <pre>
 * import numpy as np
 * import matplotlib.pyplot as plt
 *
 * points = np.array([(1,1.0), (2,.955), (3,.922), (4,.892), (5,.863), (6,.837), (7,.811), \
 * (8,.786), (9,.762), (10,.739), (11,.707), (12,.680), (13,.653), (14,.626), (15,.599)])
 * x = points[:,0]
 * y = points[:,1]
 * z = np.polyfit(x, y, 5)
 * f = np.poly1d(z)
 * x_new = np.linspace(x[0], x[-1], 50)
 * y_new = f(x_new)
 * plt.plot(x,y,'o', x_new, y_new)
 * plt.xlim([x[0]-1, x[-1] + 1 ])
 * plt.show()
 * print(f.c)
 *
 * </pre>
 */
@SuppressWarnings(Array("org.wartremover.warts.TraversableOps"))
object RpeOps {

  def toPct(reps: Int, rpe: Rpe): Double = tuschererRpeApprox(reps, rpe)

  def toRpe(reps: Int, pct: Double): Rpe = {
    val searchInterval = 5.0 to 10.0 by 0.5
    searchInterval.map(rpeVal => (rpeVal, toPct(reps, rpeVal.toDouble.rpe)))
      .minBy(t => Math.abs(t._2 - pct))._1.toDouble.rpe
  }

  def e1rm(weight: Weight, reps: Int, rpe: Rpe): Weight = {
    val pct = toPct(reps, rpe)
    Weight((weight.grams / pct).toInt)
  }

  // Sorted by degree starting from 0
  private val tuschererPolyCoefficients =
    List(1.05026154e+00, -5.61963809e-02, 5.59930936e-03, -4.32116278e-04, 1.25208383e-05, -5.72556958e-08)

  /**
   * Evaluates n-degree polynomial at point x
   *
   * @param coefficients coefficients sorted by degree
   * @param x            point
   */
  private def evaluatePolyAt(coefficients: List[Double], x: Double): Double = coefficients.reduceRight(_ + x * _)

  private def tuschererRpeApprox(reps: Int, rpe: Rpe): Double = rpe match {
      case RpeVal(rpeVal) =>
        require(reps > 0 && reps <= 15, "RPE-percentage conversion only makes sense for reps between 1 and 15")
        val x = reps.toDouble + 10.0 - rpeVal
        BigDecimal(evaluatePolyAt(tuschererPolyCoefficients, x)).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
      case _ => 0.0
    }

}
