package message

trait TypesSearchPayload {
  def searchTypes: List[String]

  def text: String
}

case class FilledSearchPayload(searchTypes: List[String] = List("country", "city", "airport"), text: String) extends TypesSearchPayload

case class Passengers (adults: Int, infants: Int = 0, children: Int = 0)

case class Segments (from: String, to: String, departureCity: String ="", arrivalCity: String="", departureCountry: String="", arrivalCountry: String="", departureParentCity: String="", arrivalParentCity: String="", date: String)

case class SearchFlightPayload(segments: Seq[Segments], passengers: Passengers, cabin: String)

