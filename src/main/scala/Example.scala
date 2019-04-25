import akka.actor.ActorSystem

object Example {


  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    // default Actor constructor    request.uri.
    val cl = new Client()
    val info = cl.getUserInfo
    println(info)
    val an = cl.getFullSearchRequests("Москва")
    println(an)
    system.terminate()
  }


}
