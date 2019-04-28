package json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import message._
import spray.json.DefaultJsonProtocol

trait JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val filledSearchPayloadFormat = jsonFormat2(FilledSearchPayload)
  implicit val uidPayloadFormat = jsonFormat1(UidPayload)
  implicit val userMessageFormat = jsonFormat3(UserMessage)

  implicit val cityNameFormat = jsonFormat2(CityName)
  implicit val coordinatesFormat = jsonFormat2(Coordinates)
  implicit val countryNameFormat = jsonFormat2(CountryName)
  implicit val highlightFormat = jsonFormat2(Highlight)
  implicit val nameFormat = jsonFormat2(Name)
  implicit val scoringFormat = jsonFormat3(Scoring)
  implicit val objectSourceFormat = jsonFormat9(ObjectSource)
  implicit val scoreObjectFormat = jsonFormat4(ScoreObject)
  implicit val fullSearchPayloadFormat = jsonFormat4(FullSearchPayload)
  implicit val fullSearchMessageFormat = jsonFormat4(FullSearchMessage)

  implicit val cityFormat = jsonFormat2(City)
  implicit val priceFormat = jsonFormat2(Price)
  implicit val pricePerPaxFormat = jsonFormat3(PricePerPax)
  implicit val departureFormat = jsonFormat4(Departure)
  implicit val carriersFormat = jsonFormat2(Carriers)
  implicit val baggageExternalFormat = jsonFormat2(BaggageExternal)
  implicit val baggageFormat = jsonFormat2(Baggage)
  implicit val flightSegmentFormat = jsonFormat12(FlightSegments)
  implicit val flightsFormat = jsonFormat2(Flights)
  implicit val infoFormat = jsonFormat4(Info)
  implicit val offersFormat = jsonFormat7(Offers)
  implicit val searchPayloadFormat = jsonFormat3(SearchPayload)
  implicit val searchMessageFormat = jsonFormat5(SearchMessage)

  implicit val passengersFormat = jsonFormat3(Passengers)
  implicit val segmentsFormat = jsonFormat9(Segments)
  implicit val searchFlightPayloadMessageFormat = jsonFormat3(SearchFlightPayload)

}




