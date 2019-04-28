import java.time.LocalDate
import java.time.temporal.ChronoUnit

import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import json.JsonProtocol
import message.{FullSearchMessage, Passengers, SearchFlightPayload, SearchMessage, Segments}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import platform.ApiMethods
import spray.json._

import scala.concurrent.duration._

class FlightSearchSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll with HttpClient with JsonProtocol {
  override val log: LoggingAdapter = Logging(system.eventStream, "flight.search.spec")

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(10 seconds)

  implicit val ec = system.dispatcher

  override protected val baseRequest: HttpRequest = ApiMethods.searchFlight

  object FlightClasses extends Enumeration {

    protected case class Val(code: String) extends super.Val {}

    val Economy = Val("Y")
    val Business = Val("C")
    val FirstClass = Val("F")
  }

  "Search in two directions" should "return result" in {
    val segments = createSegment(1, "LED", "DLA") :: createSegment(13, "DLA", "LED") :: Nil
    val body = SearchFlightPayload(segments,Passengers(adults=1),FlightClasses.Economy.code)
    val payload = body.toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[SearchMessage]) { message =>
        message.status shouldBe "Ok"
        message.payload.offers.size should be > 0
      }
    }
  }

  "Search in one direction" should "return result" in {
    val segments = createSegment(1, "LED", "DLA") ::  Nil
    val body = SearchFlightPayload(segments,Passengers(adults=1),FlightClasses.Economy.code)
    val payload = body.toJson.toString()

    val response = responseFunction(baseRequest, payload)

    whenReady(response) { r =>
      r.status shouldBe StatusCodes.OK
      whenReady(Unmarshal(r.entity).to[SearchMessage]) { message =>
        message.status shouldBe "Ok"
        message.payload.offers.size should be > 0
      }
    }
  }

  def createSegment(daysFromNow:Int, destination:String, arrival:String): Segments ={
    val now = LocalDate.now()
    val dateFrom = now.plus(daysFromNow, ChronoUnit.DAYS)
    Segments(from=destination,to=arrival, date=dateFrom.toString)
  }

}
