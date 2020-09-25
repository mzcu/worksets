package worksets

package object support {

  trait Quantity[T] extends Ordered[T]:
    def *(multiplier: Double): T
    def +(summand: T): T

  trait Monoid[A]:
    def empty: A

    def combine(first: A, second: A): A

    def combineAll(as: Iterable[A]): A = as.iterator.foldLeft(empty)(combine)


  implicit class ListMonoid[A](val as: List[A]) extends Monoid[List[A]]:
    override def empty: List[A] = Nil
    override def combine(first: List[A], second: List[A]): List[A] = first ::: second
    def combineAll(implicit m: Monoid[A]): A = m.combineAll(as)


  case class Percent(value: Int) extends AnyVal:
    def asRatio: Double = value / 100.0
    def *[T](quantity: Quantity[T]): T = quantity.*(this.asRatio)

  implicit class IntPercentOps(value: Int):
    def pct: Percent = Percent(value)

}