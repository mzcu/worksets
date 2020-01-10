package worksets.ui

import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.{Node, document}
import org.scalajs.dom.raw.{Event, Node}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * Created by on 08-01-20.
 */
@JSExportTopLevel("Main")
object Main {

  case class Contact(name: Var[String], email: Var[String])

  val data = Vars.empty[Contact]

  @dom
  def table = {
    <div>
      <button
      onclick={event: Event =>
        data.value += Contact(Var("Test"), Var("test@test.test"))}>
        Add a contact
      </button>
    </div>
      <table border="1" cellPadding="5">
        <thead>
          <tr>
            <th>Name</th>
            <th>E-mail</th>
            <th>Operation</th>
          </tr>
        </thead>
        <tbody>
          {for (contact <- data) yield {
          <tr>
            <td>
              {contact.name.bind}
            </td>
            <td>
              {contact.email.bind}
            </td>
            <td>
              <button
              onclick={_: Event =>
                contact.name := "Modified Name"}>
                Modify the name
              </button>
            </td>
          </tr>
        }}
        </tbody>
      </table>
  }



  @JSExport
  def main(container: Node) = dom.render(container, table)

}
