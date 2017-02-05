package example

import org.scalatest._
import play.api.libs.json._
import scala.io.Source

class SkopSpec extends FlatSpec with Matchers {


  val source: String = Source.fromFile("skop.json").getLines.mkString
  val result: JsValue = Json.parse(source)

  "The /skop.json API" should "have relevant parentSku and sku" in {

    val sku = result \ "sku"
    val parentSku = result \ "parentSku"

    assert(isSkuParentOf(sku.as[String], parentSku.as[String]))

  }


  def isSkuParentOf(sku: String, parentSku: String): Boolean = {

    //    I assumed an sku and parentSku should have the same first 8 characters.
    //    Changing the comparision logic is easy though.
    sku.substring(0, 7) == parentSku.substring(0, 7)

  }

}
