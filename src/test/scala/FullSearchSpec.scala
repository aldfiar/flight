import java.time.temporal.ChronoUnit
import java.time.{Instant, ZonedDateTime}

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

  override val baseRequest = appendQuery(ApiMethods.fullSearchRequests)

  "Full Search for known address" should "return non empty result" in {
    val payload = FilledSearchPayload(text = "Москва").toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[FullSearchMessage]) { message =>
        message.status shouldBe "Ok"
        checkTime(message.time)
        message.payload.sortedByScoreObjects.size should be > 0
      }
    }
  }

  "Full Search for empty address" should "return empty result" in {
    val payload = FilledSearchPayload(text = "").toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[FullSearchMessage]) { message =>
        message.status shouldBe "Ok"
        checkTime(message.time)
        checkSizeEqualsHitsCount(message, 0)
      }
    }
  }

  "Full Search for non existing address" should "return empty result" in {
    val payload = FilledSearchPayload(text = "Валинор").toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[FullSearchMessage]) { message =>
        message.status shouldBe "Ok"
        val payload = message.payload
        checkTime(message.time)
        //Как Иллинойс Валлей связан с Валинором
        checkSizeEqualsHitsCount(message, 1)
        val source = payload.sortedByScoreObjects(0).objectSource
        source.name.ru shouldBe "Иллинойс Валлей"
      }
    }
  }

  def checkSizeEqualsHitsCount(message: FullSearchMessage, size: Int): Unit = {
    val payload = message.payload
    val objectsQuantity = payload.sortedByScoreObjects.size
    objectsQuantity shouldBe size
  }

  def checkTime = (t: String) => Instant.from(ZonedDateTime.parse(t)).truncatedTo(ChronoUnit.MINUTES) shouldBe Instant.now().truncatedTo(ChronoUnit.MINUTES)

  def appendQuery(f: Map[String, String] => HttpRequest) = f(Map("context" -> "travel", "version" -> "1.3"))

  override def afterAll(): Unit = {
    system.terminate()
    log.debug("Shutdown")
  }

}