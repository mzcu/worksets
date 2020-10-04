package worksets

package object support:

  trait Ord[T]:
    def compare(x: T, y: T): Int
    extension (x: T) def < (y: T) = compare(x, y) < 0
    extension (x: T) def > (y: T) = compare(x, y) > 0
    extension (x: T) def max(y: T) = if x > y then x else y
  
  trait Quantity[T] extends Ord[T]:
    extension (x: T) def *(multiplier: Double): T
    extension (x: T) def +(summand: T): T

  trait Monoid[A]:
    def empty: A
    extension (x: A) def combine(y: A): A
  
  extension[A: Monoid] (xs: List[A]) def combineAll(): A =
    xs.foldLeft(summon[Monoid[A]].empty)(_ combine _)

  case class Percent(value: Int) extends AnyVal:
    def asRatio: Double = value / 100.0
    def *[T: Quantity](quantity: T): T = quantity * this.asRatio
    
  extension (value: Int) def pct = Percent(value)
