package platform

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._

object ApiMethods {

  private val host = "api.tinkoff.ru"

  private def createUri = (path: String) => Uri.from(scheme = "https", host = host, path = path)

  private def postRequestWithUri = (uri: Uri) => HttpRequest(POST, uri = uri)

  private def addQuery = (uri:Uri, values: Map[String, String]) => uri.withQuery(Query(values))

  private val createRequest = addQuery.tupled andThen postRequestWithUri

  def fullSearchRequests(values: Map[String, String]): HttpRequest = {
    val path = "/api/search/fulltext"
    createRequest(createUri(path), values)
  }

  def searchFlight(values: Map[String, String]): HttpRequest = {
    val path = "/travel/api/flight/search"
    createRequest(createUri(path), values)
  }
}
