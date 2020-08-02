package worksets.report

import java.awt.Desktop
import java.net.URI

/**
 * Created by on 02-08-20.
 */
object Browser {
  def browse(uri: URI): Unit = {
    require(Desktop.isDesktopSupported)
    Desktop.getDesktop.browse(uri)
  }
}
