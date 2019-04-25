package platform

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._

object ApiMethods {
  private val host = "api.tinkoff.ru"

  def userRequest(): HttpRequest = {
    val path = "/v1/webuser"
    val uri = getUri(path)
    HttpRequest(
      GET,
      uri = uri,
    )
  }

  private def getUri(path: String): Uri = {
    Uri.from(scheme = "https", host = host, path = path)
  }

  def fullSearchRequests(values: Map[String, String]): HttpRequest = {
    val path = "/api/search/fulltext"
    val uri = getUri(path).withQuery(Query(values))
    HttpRequest(
      POST,
      uri = uri,
    )
  }

  def searchFlight(values: Map[String, String]): HttpRequest = {
    val path = "/travel/api/flight/search"
    val uri = getUri(path).withQuery(Query(values))
    HttpRequest(
      POST,
      uri = uri,
    )
  }
}
