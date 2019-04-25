package message

trait SearchPayload {
  def searchTypes: List[String]

  def text: String
}

case class FilledSearchPayload(searchTypes: List[String] = List("country", "city", "airport"), text: String) extends SearchPayload