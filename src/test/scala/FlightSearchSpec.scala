import akka.event.{Logging, LoggingAdapter}
import json.JsonProtocol
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class FlightSearchSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll with HttpClient with JsonProtocol {
  override val log: LoggingAdapter = Logging(system.eventStream, "flight.search.spec")
}
