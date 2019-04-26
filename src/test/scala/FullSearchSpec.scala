import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import json.JsonProtocol
import message.{FilledSearchPayload, FullSearchMessage}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import platform.ApiMethods
import spray.json._

import scala.concurrent.duration._

class FullSearchSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll with HttpClient with JsonProtocol {

  override val log: LoggingAdapter = Logging(system.eventStream, "full.search.spec")

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(5 seconds)

  implicit val ec = system.dispatcher

  private val userAgentHeader = headers.`User-Agent`.apply("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")

  private val baseRequest = appendQuery(ApiMethods.fullSearchRequests)

  private val responseFunction =  addEntity.tupled andThen addHeader andThen sendRequest

  "Full Search for known address" should "return non empty result" in {
    val payload = FilledSearchPayload(text = "Москва").toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[FullSearchMessage]) { message =>
        message.status shouldBe "Ok"
        val payload = message.payload
        payload.sortedByScoreObjects.size shouldNot be
        0
      }
    }
  }

  "Full Search for wrong address" should "return empty result" in {
    val payload = FilledSearchPayload(text = "").toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[FullSearchMessage]) { message =>
        message.status shouldBe "Ok"
        val payload = message.payload
        payload.sortedByScoreObjects.size shouldBe 0
      }
    }
  }

  def appendQuery(f: Map[String, String] => HttpRequest) = f(Map("context" -> "travel", "version" -> "1.3"))

  def addHeader = (h: HttpRequest) => h.withHeaders(List(userAgentHeader))

  def addEntity = (h: HttpRequest, payload: String) => h.withEntity(HttpEntity(ContentTypes.`application/json`, payload))

  override def afterAll(): Unit = {
    system.terminate()
    log.debug("Shutdown")
  }

}