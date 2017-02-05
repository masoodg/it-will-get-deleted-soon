package example

import java.util.Date
import org.scalatest._
import play.api.libs.json._
import scala.io.Source

class KinxSpec extends FlatSpec with Matchers {

  val source: String = Source.fromFile("kinx.json").getLines.mkString
  val result: JsValue = Json.parse(source)


  "Calling /kinx.json API" should "have a createdAt field in the past" in {

    val e = (result \ "createdAt").validate[Date]
    assert(e.get.before(new Date()))

  }

  "Calling /kinx.json API" should "have max 5 as averageRating" in {

    val rating = (result \ "averageRating").as[Double]
    assert(rating <= 5.0)

  }
}
