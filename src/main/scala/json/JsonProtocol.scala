package json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import message._
import spray.json.DefaultJsonProtocol

trait JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val filledSearchPayloadFormat = jsonFormat2(FilledSearchPayload)
  implicit val uidPayloadFormat = jsonFormat1(UidPayload)
  implicit val userMessageFormat = jsonFormat3(UserMessage)
  implicit val cityFormat = jsonFormat2(CityName)
  implicit val coordinatesFormat = jsonFormat2(Coordinates)
  implicit val countryNameFormat = jsonFormat2(CountryName)
  implicit val highlightFormat = jsonFormat2(Highlight)
  implicit val nameFormat = jsonFormat2(Name)
  implicit val scoringFormat = jsonFormat3(Scoring)
  implicit val objectSourceFormat = jsonFormat9(ObjectSource)
  implicit val scoreObjectFormat = jsonFormat4(ScoreObject)
  implicit val fullSearchPayloadFormat = jsonFormat4(FullSearchPayload)
  implicit val fullSearchMessageFormat = jsonFormat4(FullSearchMessage)
}




