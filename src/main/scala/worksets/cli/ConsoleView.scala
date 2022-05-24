package worksets.cli

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import worksets._

import scala.annotation.tailrec

trait Show[A]:
  extension (a: A) def show: String


object ConsoleView {

  given DateShow: Show[LocalDate] = _.format(DateTimeFormatter.ofPattern("E, dd-MM-yyyy"))

  given weightShow: Show[Weight] = it =>
    if it.grams < 1_000_000 then
      "%.1f kg".format(it.grams / 1000.0)
    else
      "%.1f t".format(it.grams / 1_000_000.0)

  given settShow: Show[Set] =
    case Set(_, 0, _) => "Not done"
    case it => s"${it.weight.show} x ${it.reps}" ++ (it.rpe match {
      case RpeVal(value) => " @ " + value.toString
      case _ => ""
    })

  given workSetShow: Show[WorkSet] = (workset: WorkSet) => s"${workset.actual.show}".padTo(30, ' ')

  given exerciseWithModsShow: Show[ExerciseWithMods] = { (it: ExerciseWithMods) =>
    @tailrec
    def showMods(base: String, mods: worksets.Mods): String =
      (base, mods) match
        case (base, NoMods) => base
        case (base, m@WithMods(Some(tempo), _, _, _)) => showMods(s"${tempo.show} $base", m.copy(tempo = None))
        case (base, m@WithMods(_, _, Some(grip), _)) => showMods(s"$grip $base", m.copy(grip = None))
        case (base, m@WithMods(_, _, _, Some(kit))) => showMods(s"$base /w $kit", m.copy(kit = None))
        case (base, m@WithMods(_, Some(stance), _, _)) => showMods(s"$base /w $stance", m.copy(stance = None))
        case _ => base
    val name = it.exercise.show
    val barType = it.barType.show
    val base = s"$barType $name".trim
    showMods(base, it.mods)
  }

  given barTypeShow: Show[BarType] =
    case TrapBar => "Trap Bar"
    case _ => ""

  given exerciseShow: Show[Exercise] = (exercise: Exercise) => exercise.name

  given tempoShow: Show[TempoMod] =
    case Tempo => "Tempo"
    case TouchAndGo => "Touch-and-go"
    case Pause(ct) => s"${ct}ct Pause"
    case CustomTempoMod(e, i, c) => s"$e$i$c Tempo"

  given workoutShow: Show[Workout] =
    case Workout(_, sets) => sets.groupBy(_.exercise).toList.sortBy(_._2.headOption.map(_.ord).orElse(Some(0)))
      .map((t: (ExerciseWithMods, List[WorkSet])) => s"${t._1.show}\n    " + t._2.map(_.show).mkString("\n    ")).mkString("\n")

}