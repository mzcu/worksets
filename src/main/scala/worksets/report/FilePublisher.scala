package worksets.report

import java.nio.file.{Files, Path, Paths}

import fansi.Str
import worksets.lib.Fansi._

/**
 * Created by on 02-08-20.
 */
object FilePublisher:
  def publish(name: String, content: String): Path =
    val html = s"<pre>${Str(content).toHtml}</pre>"
    Files.write(Files.createFile(Paths.get(s"/tmp/$name.html")), html.getBytes())
