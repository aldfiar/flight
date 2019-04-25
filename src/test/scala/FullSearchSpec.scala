import akka.event.Logging
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import json.JsonProtocol
import message.{FilledSearchPayload, FullSearchMessage}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import platform.ApiMethods
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps

class FullSearchSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll with HttpClient with JsonProtocol {
  private val log = Logging(system.eventStream, "full.search.spec")
  implicit val ec = system.dispatcher
  private val userAgentHeader = headers.`User-Agent`.apply("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(10 seconds)

  "Full Search" should "return search travel result" in {
    val query = Map("context" -> "travel", "version" -> "1.3")
    val baseRequest: HttpRequest = ApiMethods.fullSearchRequests(query)
    val payload = FilledSearchPayload(text = "Москва").toJson.toString()

    val entity = HttpEntity(ContentTypes.`application/json`, payload)
    val request = baseRequest.withEntity(entity).withHeaders(List(userAgentHeader))
    val response = process(request)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[FullSearchMessage]) { message =>
        message.status shouldBe "Ok"
      }
    }
  }

  override def afterAll(): Unit = {
    system.terminate()
    log.debug("Shutdown")
  }


}