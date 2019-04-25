package message

trait Message[Z] {
  def payload: Z
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

case class FullSearchMessage(payload: FullSearchPayload, trackingId: String, time: String, status: String) extends Message[FullSearchPayload]
