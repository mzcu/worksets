package worksets.repository

import java.nio.file.{Files, Path, Paths}
import java.time.LocalDate
import java.util.Comparator
import java.util.concurrent.atomic.AtomicInteger

import worksets.{Workout, WorkoutHistory}
import worksets.workouts.WorkoutRepository

import scala.collection.immutable.Seq
import scala.jdk.OptionConverters._

// Single-user data store
object ObjectStore extends WorkoutRepository {

  import io.circe._, io.circe.generic.auto._, io.circe.syntax._

  private val dbVersion = 0.1
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  private val dataDirLocation = sys.props.get("user.home").get + s"/.worksets/${dbVersion.toString}"
  private val dataDir: Path = Files.createDirectories(Paths.get(dataDirLocation))
  private val dbFilePrefix = "workouts-db-"
  private val dbFileExt = ".json"
  private val currentVersion: AtomicInteger = new AtomicInteger
  private val emptyStore = ObjectStore(dbVersion, Seq.empty)

  override def load(): WorkoutHistory = {
    lastRevision().fold(Seq.empty[worksets.Workout]) { path =>
      val loaded = jawn.parseFile(path.toFile).getOrElse(Json.Null).as[ObjectStore].getOrElse(emptyStore).workouts
      currentVersion.set(path.getFileName.getFileName.toString.split(dbFilePrefix).reverse.headOption.getOrElse("0").replace(dbFileExt, "").toInt)
      loaded
    }
  }

  override def store(workouts: WorkoutHistory): Int = {
    val nextVersion = currentVersion.incrementAndGet()
    @SuppressWarnings(Array("org.wartremover.warts.Any"))
    val dbFile = Files.createFile(dataDir.resolve(f"$dbFilePrefix$nextVersion%06d$dbFileExt"))
    val dbObject = ObjectStore(dbVersion, workouts)
    val _ = Files.write(dbFile, dbObject.asJson.toString().getBytes())
    nextVersion
  }

  private def lastRevision(): Option[Path] =
    Files.list(dataDir)
      .filter(p => p.getName(p.getNameCount - 1).toString.startsWith(dbFilePrefix))
      .sorted(Comparator.reverseOrder()).findFirst().toScala


  def main(args: Array[String]): Unit = {
    println(load())
    val _ = store(Seq(Workout(LocalDate.now(), List())))
    println(load())
  }
}

private[repository] case class ObjectStore(version: Double, workouts: Seq[Workout])