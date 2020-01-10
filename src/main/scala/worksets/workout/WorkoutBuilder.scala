package worksets.workout

import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

import worksets._
import worksets.rpe.RpeOps

import scala.collection.mutable.ListBuffer

/**
 * Created by on 04-01-20.
 */
class WorkoutBuilder(val date: LocalDate) {

  private[workout] val setCounter = new AtomicInteger()
  private[workout] val workSets = ListBuffer[WorkSet]()

  def exercise(exercise: ExerciseWithMods): WorkoutExerciseBuilder = new WorkoutExerciseBuilder(this, exercise)

  def endWorkout(): Workout = Workout(date, workSets.toList)
}

@SuppressWarnings(Array("org.wartremover.warts.All"))
class WorkoutExerciseBuilder(private val parent: WorkoutBuilder, private val exercise: ExerciseWithMods) {


  private[workout] val workSets = ListBuffer[WorkSet]()

  def withWorkingSet(weight: Weight, reps: Int, rpe: Rpe): this.type = {
    val number = parent.setCounter.incrementAndGet()
    val target = Set(weight, reps, rpe)
    workSets += WorkSet(exercise, target, target, number)
    this
  }

  def withWorkingSetsByRpe(weight: Weight, reps: Int, rpe: Range): this.type = {
    val startingPct = RpeOps.toPct(reps, RpeVal(rpe.head))
    workSets ++= rpe.map(rpeInt => {
      val number = parent.setCounter.incrementAndGet()
      val currentPct = RpeOps.toPct(reps, RpeVal(rpeInt))
      val percentDiff = {
        val pdiff = currentPct - startingPct
        if (pdiff < 0) 1 - pdiff else 1 + pdiff
      }
      val adjustedWeight = Weight((weight.grams * percentDiff).toInt)
      val target = WorksetOps.createSet(adjustedWeight, reps, RpeVal(rpeInt))
      WorkSet(exercise, target, target, number)
    })
    this
  }

  def withWorkingSetRelative(multiplier: Double, reps: Int, howMany: Int): this.type = {
    val number = parent.setCounter.incrementAndGet()
    require(workSets.nonEmpty)
    val reference = workSets.last.target
    val modifiedWeight = Weight((reference.weight.grams * multiplier).toInt)
    val modifiedPct = RpeOps.toPct(reference.reps, reference.rpe) * multiplier
    val modifiedRpe = RpeOps.toRpe(reps, modifiedPct)
    val target = WorksetOps.createSet(modifiedWeight, reps, modifiedRpe)
    workSets ++= List.fill(howMany)(WorkSet(exercise, target, target, number))
    this
  }

  def loadDrop(howMany: Int): this.type = {
    val number = parent.setCounter.incrementAndGet()
    require(workSets.nonEmpty)
    val reference = workSets.last
    val target = WorksetOps.roundSet(reference.target.copy(weight = Weight((reference.target.weight.grams * .95).toInt)))
    workSets ++= List.fill(howMany)(reference.copy(target = target, actual = target, ord = number))
    this
  }

  def end(): WorkoutBuilder = {
    parent.workSets ++= this.workSets
    parent
  }

}

object WorkoutBuilder {
  def newWorkout(date: String): WorkoutBuilder = WorkoutBuilder(LocalDate.parse(date))

  def apply(date: LocalDate): WorkoutBuilder = new WorkoutBuilder(date)
}

