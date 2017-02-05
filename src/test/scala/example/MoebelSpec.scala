package example

import org.scalatest._
import play.api.libs.json._
import play.api.libs.functional.syntax._


import scala.io.Source

class MoebelSpec extends FlatSpec with Matchers {

  val source: String = Source.fromFile("moebel.json").getLines.mkString
  val result = Json.parse(source)

  case class PriceRange(min: String, max: String)

  implicit val regularPriceReads: Reads[PriceRange] = (
    (JsPath \ "min").read[String] and
      (JsPath \ "max").read[String]
    ) (PriceRange.apply _)

  // This test is based on my assumption that this API should return at last 1 result
  // which based on the user stories might be different.
  "Calling /moebel.json API" should "return at least 1 result" in {

    assert((result \\ "results").nonEmpty)

  }

  "All of the results of /moebel.json API" should "have non empty sku of type String" in {

    val prices = (result \\ "sku").map(_.validate[String])
    val skus = prices.map {
      case e: JsError =>
        println("Errors: " + JsError.toFlatJson(e).toString())
        "not valid type"
      case s: JsSuccess[String] if !s.get.contains("") => "empty sku"
      case s: JsSuccess[String] => s.get
    }

    assert(!skus.contains("not valid priceRange"))
    assert(!skus.contains("empty sku"))

  }

  "All of the results of /moebel.json API" should "have valid price range which is (mix: String, max: String)" in {

    val prices = (result \\ "priceRange").map(_.validate[PriceRange])
    assert(!prices.map {
      case e: JsError =>
        println("Errors: " + JsError.toFlatJson(e).toString())
        "not valid priceRange"
      case s: JsSuccess[PriceRange] => s.get
    }.contains("not valid priceRange"))

  }

}
