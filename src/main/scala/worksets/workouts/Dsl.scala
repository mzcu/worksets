package worksets.workouts

import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

import worksets._
import worksets.rpe.RpeOps
import worksets.support.{IntPercentOps, Percent}

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions


object Dsl {

  class WorkoutBuilder(val date: LocalDate, val workoutHistory: WorkoutHistory) {
    private[workouts] val setCounter = new AtomicInteger()
    private[workouts] val workSets = ListBuffer[WorkSet]()

    def exercise(exercise: ExerciseWithMods): WorkoutExerciseBuilder = new WorkoutExerciseBuilder(this, exercise)
  }

  @SuppressWarnings(Array("org.wartremover.warts.All"))
  class WorkoutExerciseBuilder(private[workouts] val parent: WorkoutBuilder, private[workouts] val exercise: ExerciseWithMods) {
    self =>

    private var closed: Boolean = false
    private[workouts] val workSets = ListBuffer[WorkSet]()

    def close(): Unit = closed = true

    def isOpen: Boolean = !closed

    // Static builders using values provided as arguments)

    def workset(weight: Weight): RepsToRpeBuilder = new RepsToRpeBuilder {
      require(isOpen)
      private val number = parent.setCounter.incrementAndGet()

      def x(reps: Int): RpeBuilder = (rpe: Rpe) => {
        val target = Set(weight, reps, rpe)
        workSets += WorkSet(exercise, target, target, number)
        self
      }
    }

    def repeat(times: Int): WorkoutExerciseBuilder =
      this worksetRelative 100.pct x workSets.last.target.reps sets times

    def worksetRelative(multiplier: Percent): RepsToSetsBuilder = (reps: Int) => (howMany: Int) => {
      require(isOpen)
      val number = parent.setCounter.incrementAndGet()
      require(workSets.nonEmpty)
      val reference = workSets.last.target
      val modifiedWeight = multiplier * reference.weight
      val modifiedPct = RpeOps.toPct(reference.reps, reference.rpe) * multiplier.asRatio
      val modifiedRpe = RpeOps.toRpe(reps, modifiedPct)
      val target = WorksetOps.createSet(modifiedWeight, reps, modifiedRpe)
      workSets ++= List.fill(howMany)(WorkSet(exercise, target, target, number))
      self
    }

    def worksetsByRpe(rpe: Range): RepsToWeightBuilder = (reps: Int) => (weight: Weight) => {
      require(isOpen)
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


    def loadDrop(howMany: Int): this.type = {
      require(isOpen)
      val number = parent.setCounter.incrementAndGet()
      require(workSets.nonEmpty)
      val reference = workSets.last
      val target = WorksetOps.roundSet(reference.target.copy(weight = Weight((reference.target.weight.grams * .95).toInt)))
      workSets ++= List.fill(howMany)(reference.copy(target = target, actual = target, ord = number))
      this
    }


    // Dynamic builders using workout history to calculate targets

    def weeklyProgressiveOverload(multiplier: Double): this.type = {
      require(isOpen)
      val number = parent.setCounter.incrementAndGet()
      val lastTopSet = parent.workoutHistory.reverse.take(10).map(_.sets.filter(_.exercise == this.exercise).map(_.actual).maxBy(_.weight.grams)).head
      val target = WorksetOps.createSet(Weight((lastTopSet.weight.grams * multiplier).toInt), lastTopSet.reps, lastTopSet.rpe)
      workSets += WorkSet(exercise, target, target, number)
      this
    }

    def worksetByE1RM(percent: Percent): RepsBuilder = (reps: Int) => {
      require(isOpen)
      val e1rm = parent.workoutHistory.filter(_.sets.exists(_.exercise == exercise)).takeRight(10)
        .flatMap(WorkoutStats.e1rmPerExercise).filter(_._1 == exercise).minBy(-_._2.grams)._2
      val weight = percent*e1rm
      val newRpe = RpeOps.toRpe(reps, percent.asRatio)
      val target = WorksetOps.createSet(weight, reps, newRpe)
      val number = parent.setCounter.incrementAndGet()
      workSets += WorkSet(self.exercise, target, target, number)
      self
    }

  }

  def workout(implicit workoutHistory: WorkoutHistory): WorkoutDateBuilder =
    (date: LocalDate) => new WorkoutBuilder(date, workoutHistory)

  trait RepsToRpeBuilder {
    def x(reps: Int): RpeBuilder
  }


  trait RepsToSetsBuilder {
    def x(reps: Int): SetRepetitionBuilder
  }

  trait SetRepetitionBuilder {
    def sets(howMany: Int): WorkoutExerciseBuilder
  }

  trait RpeBuilder {
    def at(rpe: Rpe): WorkoutExerciseBuilder
  }

  trait RepsBuilder {
    def x(reps: Int): WorkoutExerciseBuilder
  }

  trait WeightBuilder {
    def weight(weight: Weight): WorkoutExerciseBuilder
  }

  trait RepsToWeightBuilder {
    def x(reps: Int): WeightBuilder
  }

  trait WorkoutDateBuilder {
    def on(localDate: LocalDate): WorkoutBuilder
  }

  implicit def exerciseToWorkoutBuilder(value: WorkoutExerciseBuilder): WorkoutBuilder = {
    require(value.isOpen)
    val _ = value.parent.workSets ++= value.workSets
    value.parent
  }

  implicit def workoutExerciseBuilderToWorkout(value: WorkoutExerciseBuilder): Workout = {
    val workoutBuilder = if (value.isOpen) exerciseToWorkoutBuilder(value) else value.parent
    Workout(workoutBuilder.date, workoutBuilder.workSets.toList)
  }
}

