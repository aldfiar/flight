import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import json.JsonProtocol
import message._
import org.slf4j.LoggerFactory
import platform.ApiMethods
import spray.json._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * TODO in scala way
  **/
class Client extends JsonProtocol {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  private val logger = LoggerFactory.getLogger(classOf[Client])
  private val userAgentHeader = headers.`User-Agent`.apply("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")
  private val timeout: Duration = 5 seconds
  private val headerList: ListBuffer[HttpHeader] = ListBuffer(userAgentHeader)

  def getUserInfo: UserMessage = {
    val response = process(ApiMethods.userRequest())
    deserialize[UserMessage](response.entity)
  }

  def getFullSearchRequests(text: String): FullSearchMessage = {
    val query = Map("context" -> "travel", "version" -> "1.3")
    val baseRequest: HttpRequest = ApiMethods.fullSearchRequests(query)
    val payload = FilledSearchPayload(text = text).toJson.toString()

    val entity = HttpEntity(ContentTypes.`application/json`, payload)

    logger.debug("entity: {}", entity)

    val request = baseRequest.withEntity(entity)
    val response = process(request)

    deserialize[FullSearchMessage](response.entity)
  }

  def process(request: HttpRequest): HttpResponse = {
    val requestWithHeaders = request.withHeaders(headerList.toList)

    val future = Http().singleRequest(requestWithHeaders)
    future.onComplete {
      case Success(res) => res
      case Failure(e) => logger.error("Wrong response", e.fillInStackTrace())
    }

    Await.result(future, timeout)
  }

  private def deserialize[T](value: ResponseEntity)(implicit um: Unmarshaller[ResponseEntity, T]): T = {
    val convertedMessage = Unmarshal(value).to[T]

    convertedMessage.onComplete {
      case Success(message) => message
      case Failure(e) => logger.error("Deserialize error", e.fillInStackTrace())
    }

    Await.result(convertedMessage, timeout)
  }


}
