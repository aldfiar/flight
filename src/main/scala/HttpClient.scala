import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, ResponseEntity, headers}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import platform.ApiMethods

import scala.concurrent.{ExecutionContextExecutor, Future}

trait HttpClient {
  implicit val system: ActorSystem = ActorSystem("test-client")

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  protected val userAgentHeader = headers.`User-Agent`("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")

  protected val log: LoggingAdapter

  protected val baseRequest:HttpRequest

  protected val responseFunction = addEntity.tupled andThen addHeader andThen sendRequest

  protected def sendRequest = (request: HttpRequest) => Http().singleRequest(request)

  private def deserialize[T](value: ResponseEntity)(implicit um: Unmarshaller[ResponseEntity, T]): Future[T] = {
    Unmarshal(value).to[T]
  }

  protected def addHeader = (h: HttpRequest) => h.withHeaders(List(userAgentHeader))

  protected def addEntity = (h: HttpRequest, payload: String) => h.withEntity(HttpEntity(ContentTypes.`application/json`, payload))

}

