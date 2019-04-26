import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, ResponseEntity}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContextExecutor, Future}

trait HttpClient {
  implicit val system: ActorSystem = ActorSystem("test-client")

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val log: LoggingAdapter

  def sendRequest = (request: HttpRequest) => Http().singleRequest(request)

  private def deserialize[T](value: ResponseEntity)(implicit um: Unmarshaller[ResponseEntity, T]): Future[T] = {
    Unmarshal(value).to[T]
  }

}

