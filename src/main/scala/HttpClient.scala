import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, ResponseEntity}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContextExecutor, Future}

trait HttpClient {
  implicit val system: ActorSystem = ActorSystem("test-client")

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def process(request: HttpRequest): Future[HttpResponse] = {
    Http().singleRequest(request)
  }

  private def deserialize[T](value: ResponseEntity)(implicit um: Unmarshaller[ResponseEntity, T]): Future[T] = {
    Unmarshal(value).to[T]
  }

}

