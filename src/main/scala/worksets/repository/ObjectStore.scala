package worksets.repository

import java.nio.file.{Files, Path, Paths}
import java.time.LocalDate
import java.util.Comparator
import java.util.concurrent.atomic.AtomicInteger

import io.circe.Decoder.Result
import io.circe.Json.JString
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, HCursor, Json, JsonDouble, JsonNumber, JsonObject}
import worksets._
import worksets.workouts.WorkoutRepository

import scala.collection.immutable.Seq
import scala.jdk.OptionConverters._

// Single-user data store
object ObjectStore extends WorkoutRepository:

  import io.circe._, io.circe.syntax._, io.circe.Decoder.decodeLocalDate
  import CirceCodecs.given

  private val dbVersion = 0.1
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  private val dataDirLocation = sys.props.get("user.home").get + s"/.worksets/${dbVersion.toString}"
  private val dataDir: Path = Files.createDirectories(Paths.get(dataDirLocation))
  private val dbFilePrefix = "workouts-db-"
  private val dbFileExt = ".json"
  private val currentVersion: AtomicInteger = new AtomicInteger
  private val emptyStore = ObjectStore(dbVersion, Seq.empty)

  override def load(): WorkoutHistory =
    lastRevision().fold(Seq.empty[worksets.Workout]) { path =>
      val loaded = jawn.parseFile(path.toFile).getOrElse(Json.Null).as[ObjectStore].getOrElse(emptyStore).workouts
      currentVersion.set(path.getFileName.getFileName.toString.split(dbFilePrefix).reverse.headOption.getOrElse("0").replace(dbFileExt, "").toInt)
      loaded
    }

  override def store(workouts: WorkoutHistory): Int =
    val nextVersion = currentVersion.incrementAndGet()
    @SuppressWarnings(Array("org.wartremover.warts.Any"))
    val dbFile = Files.createFile(dataDir.resolve(f"$dbFilePrefix$nextVersion%06d$dbFileExt"))
    val dbObject = ObjectStore(dbVersion, workouts)
    val _ = Files.write(dbFile, dbObject.asJson.toString().getBytes())
    nextVersion

  private def lastRevision(): Option[Path] =
    Files.list(dataDir)
      .filter(p => p.getName(p.getNameCount - 1).toString.startsWith(dbFilePrefix))
      .sorted(Comparator.reverseOrder()).findFirst().toScala


  def main(args: Array[String]): Unit =
    println(load())
    val _ = store(Seq(Workout(LocalDate.now(), List())))
    println(load())

private[repository] case class ObjectStore(version: Double, workouts: Seq[Workout])

object CirceCodecs {

  import worksets._

  given encodeKitMod: Encoder[KitMod] = Encoder.instance {
    case worksets.Belt => JsonObject("Belt" -> JsonObject.empty.asJson).asJson
  }
  given decodeKitMod: Decoder[KitMod] = (c: HCursor) => {
    for
      _ <- c.downField("Belt").as[Unit]
    yield Belt
  }


  given encodeStanceMod: Encoder[StanceMod] = Encoder.instance {
    case worksets.FeetUp => JsonObject("FeetUp" -> JsonObject.empty.asJson).asJson
    case worksets.Deficit => JsonObject("Deficit" -> JsonObject.empty.asJson).asJson
  }

  given decodeStanceMod: Decoder[StanceMod] = (c: HCursor) => {
    val case1 = for
      _ <- c.downField("FeetUp").as[Unit]
    yield FeetUp
    val case2 = for
      _ <- c.downField("Deficit").as[Unit]
    yield Deficit
    case1.orElse(case2)
  }

  given encodeGripMod: Encoder[GripMod] = Encoder.instance {
    case worksets.VeryCloseGrip => JsonObject("VeryCloseGrip" -> JsonObject.empty.asJson).asJson
    case worksets.CloseGrip => JsonObject("CloseGrip" -> JsonObject.empty.asJson).asJson
    case worksets.WideGrip => JsonObject("WideGrip" -> JsonObject.empty.asJson).asJson
    case worksets.VeryWideGrip => JsonObject("VeryWideGrip" -> JsonObject.empty.asJson).asJson
  }

  given decodeGripMod: Decoder[GripMod] = (c: HCursor) => {
    val case1 = for
      _ <- c.downField("VeryCloseGrip").as[Unit]
    yield VeryCloseGrip
    val case2 = for
      _ <- c.downField("CloseGrip").as[Unit]
    yield CloseGrip
    val case3 = for
      _ <- c.downField("WideGrip").as[Unit]
    yield WideGrip
    val case4 = for
      _ <- c.downField("VeryWideGrip").as[Unit]
    yield VeryWideGrip
    case1.orElse(case2).orElse(case3).orElse(case4)
  }

  given encodeTempoMod: Encoder[TempoMod] = Encoder.instance {
    case worksets.Tempo => JsonObject("Tempo" -> JsonObject.empty.asJson).asJson
    case worksets.TouchAndGo => JsonObject("TouchAndGo" -> JsonObject.empty.asJson).asJson
    case Pause(ct) => JsonObject("Pause" -> JsonObject("ct" -> ct.asJson).asJson).asJson
    case CustomTempoMod(eccentric, isometric, concentric) =>JsonObject("CustomTempoMod" ->
      JsonObject("eccentric" -> eccentric.asJson, "isometric" -> isometric.asJson, "concentric" -> concentric.asJson).asJson).asJson
  }
  given decodeTempoMod: Decoder[TempoMod] = (c: HCursor) => {
    val case1 = for
      _ <- c.downField("Tempo").as[Unit]
    yield Tempo
    val case2 = for
      _ <- c.downField("TouchAndGo").as[Unit]
    yield TouchAndGo
    val case3 = for
      ct <- c.downField("Pause").downField("ct").as[Int]
    yield Pause(ct)
    val case4 = for
      ecc <- c.downField("CustomTempoMod").downField("eccentric").as[Int]
      iso <- c.downField("CustomTempoMod").downField("isometric").as[Int]
      con <- c.downField("CustomTempoMod").downField("concentric").as[Int]
    yield CustomTempoMod(ecc, iso, con)
    case1.orElse(case2).orElse(case3).orElse(case4)
  }

  given encodeMods: Encoder[Mods] = Encoder.instance {
    case worksets.NoMods => JsonObject("NoMods" -> JsonObject.empty.asJson).asJson
    case ms @ WithMods(_, _, _, _) => JsonObject("WithMods" -> ms.asJson).asJson
  }

  given decodeMods: Decoder[Mods] = (c: HCursor) => {
    val case1 = for
      _ <- c.downField("NoMods").as[Unit]
    yield NoMods
    val case2 = for
      value <- c.downField("WithMods").as[WithMods]
    yield value
    case1.orElse(case2)
  }

  given encodeBarType: Encoder[BarType] = Encoder.instance {
    case worksets.Barbell => JsonObject("Barbell" -> JsonObject.empty.asJson).asJson
    case worksets.TrapBar => JsonObject("TrapBar" -> JsonObject.empty.asJson).asJson
    case worksets.NoBar => JsonObject("NoBar" -> JsonObject.empty.asJson).asJson
  }
  given decodeBarType: Decoder[BarType] = (c: HCursor) => {
    val case1 = for
      _ <- c.downField("Barbell").as[Unit]
    yield Barbell
    val case2 = for
      _ <- c.downField("TrapBar").as[Unit]
    yield TrapBar
    val case3 = for
      _ <- c.downField("NoBar").as[Unit]
    yield NoBar
    case1.orElse(case2).orElse(case3)
  }

  given encodeWithMods: Encoder[WithMods] = Encoder.forProduct4("tempo", "stance", "grip", "kit")(o => (o.tempo, o.stance, o.grip, o.kit))
  given decodeWithMods: Decoder[WithMods] = Decoder.forProduct4("tempo", "stance", "grip", "kit")(WithMods.apply)

  given encodeExercise: Encoder[Exercise] = Encoder.forProduct1("name")((_.name))
  given decodeExercise: Decoder[Exercise] = Decoder.forProduct1("name")(Exercise.apply)

  given encodeExerciseWithMods: Encoder[ExerciseWithMods] = Encoder.forProduct3("exercise", "barType", "mods")(o => (o.exercise, o.barType, o.mods))
  given decodeExerciseWithMods: Decoder[ExerciseWithMods] = Decoder.forProduct3("exercise", "barType", "mods")(ExerciseWithMods.apply)
  
  given encodeRpe: Encoder[Rpe] = Encoder.instance {
    case RpeVal(v) => JsonObject("RpeVal" -> JsonObject("value" -> Json.fromDouble(v).get).asJson).asJson
    case _ => JsonObject("RpeUndefined" -> JsonObject.empty.asJson).asJson
  }

  given decodeRpe: Decoder[Rpe] = (c: HCursor) => {
    val rpeVal = for
      value <- c.downField("RpeVal").downField("value").as[Double]
    yield RpeVal(value)
    val rpeUndefined = for
      _ <- c.downField("RpeUndefined").as[Unit]
    yield RpeUndefined
    rpeVal.orElse(rpeUndefined)
  }

  given encodeWeight: Encoder[Weight] = Encoder.forProduct1("grams")((_.grams))
  given decodeWeight: Decoder[Weight] = Decoder.forProduct1("grams")(Weight.apply)

  given encodeSet: Encoder[Set] = Encoder.forProduct3("weight", "reps", "rpe")(o => (o.weight, o.reps, o.rpe))
  given decodeSet: Decoder[Set] = Decoder.forProduct3("weight", "reps", "rpe")(Set.apply)
  
  given encodeWorkSet: Encoder[WorkSet] = Encoder.forProduct5("exercise", "target", "actual", "ord", "completed")(o => (o.exercise, o.target, o.actual, o.ord, o.completed))
  given decodeWorkSet: Decoder[WorkSet] = Decoder.forProduct5("exercise", "target", "actual", "ord", "completed")(WorkSet.apply)
  
  given encodeWorkout: Encoder[Workout] = Encoder.forProduct2("date", "sets")(o => (o.date, o.sets))
  given decodeWorkout: Decoder[Workout] = Decoder.forProduct2("date", "sets")(Workout.apply)

  given encodeObjectStore: Encoder[ObjectStore] = Encoder.forProduct2("version", "workouts")(o => (o.version, o.workouts))
  given decodeObjectStore: Decoder[ObjectStore] = Decoder.forProduct2("version", "workouts")(ObjectStore.apply)

}