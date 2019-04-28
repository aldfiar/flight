package message

trait Message[Z] {
  val payload: Z
}

trait TrackMessage[Z] extends Message[Z] {
  val trackingId: String
  val time: String
  val status: String
}

case class UidPayload(wuid: String)

case class UserMessage(resultCode: String, payload: UidPayload, trackingId: String) extends Message[UidPayload]

case class CityName(ru: String, en: String)

case class Coordinates(lat: Double, lon: Double)

case class CountryName(ru: String, en: String)

case class Highlight(field: String, value: String)

case class Name(ru: String, en: String)

case class Scoring(importance: String, traffic: Option[Int], countryImportance: String)

case class ObjectSource(coordinates: Coordinates, name: Name, scoring: Scoring, code: String, country_code: String, city_name: CityName, `type`: String, city_code: String, country_name: CountryName)

case class ScoreObject(highlights: List[Highlight], score: Double, objectType: String, objectSource: ObjectSource)

case class FullSearchPayload(tookInMillis: Int, hitsCount: Int, sortedByScoreObjects: List[ScoreObject], suggest: Option[List[String]])

case class FullSearchMessage(payload: FullSearchPayload, trackingId: String, time: String, status: String) extends TrackMessage[FullSearchPayload]

case class City(name: String, country: String)

case class Price(amount: String, currency: String)

case class PricePerPax(adult: Price, child: Option[String], infant: Option[String])

case class Departure(city: String, airport: String, terminal: Option[String], time: String)

case class Carriers(operating: String, marketing: String)

case class BaggageExternal(amount: Int, unit_desc: String)

case class Baggage (count: Int, baggage: BaggageExternal)

case class FlightSegments(number: Int, duration: Int, vehicle: String, availability: Int, cabin: String, booking_class: String, ancillary_services: Option[String], carriers: Carriers, departure: Departure, baggage: Seq[Baggage], arrival: Departure, technical_stops: Option[Seq[String]])

case class Flights(flight_segments: Seq[FlightSegments], duration: Int)

case class Info(carrierNames: Map[String, String], vehicleNames: Map[String, String], cities: Map[String, City], airportNames: Map[String, String])

case class Offers(uuid: String, price: Price, flights: Seq[Int], alliance: String, price_per_pax: PricePerPax, validating_carrier: String, refundable: Boolean)

case class SearchPayload(offers: Seq[Offers], flights: Seq[Flights], info: Info)

case class SearchMessage(payload: SearchPayload, detachKey: Option[String], trackingId: String, time: String, status: String) extends TrackMessage[SearchPayload]
