package worksets.lib

import fansi.{Bold, Underlined}
import scalatags.Text.all._

import scala.collection.mutable


/**
 * Based on https://gist.github.com/lihaoyi/f0545f714d105f30afd5f4191997e3ea
 */
object Fansi:

  extension (value: fansi.Str):

    /**
     * Currently supports only bold and underline
     *
     * @return html string
     */
    def toHtml: String =

      val wrapped = mutable.Buffer.empty[scalatags.Text.Frag]
      val parsed = value
      val chars = parsed.getChars
      val colors = parsed.getColors

      var i = 0
      var previousColor = 0
      val snippetBuffer = new mutable.StringBuilder()

      def createSnippet() =
        val bold = fansi.Bold.lookupAttr(previousColor & fansi.Bold.mask) match
          case Bold.On => "bold"
          case _ => "normal"
        val underline = fansi.Underlined.lookupAttr(previousColor & fansi.Underlined.mask) match
          case Underlined.On => "underline"
          case _ => "none"
        val snippet = snippetBuffer.toString
        snippetBuffer.clear()
        wrapped.append(span(
          fontWeight := bold,
          textDecoration := underline,
          snippet
        ))

      while i < parsed.length do
        if colors(i) != previousColor && snippetBuffer.nonEmpty then createSnippet()
        previousColor = colors(i).toInt
        snippetBuffer += chars(i)
        i += 1
      createSnippet()
      wrapped.toVector.render

