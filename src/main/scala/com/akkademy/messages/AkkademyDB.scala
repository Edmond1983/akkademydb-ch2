package com.akkademy

import com.akkademy.messages._
import akka.actor.Actor
import akka.event.Logging
import scala.collection.mutable.HashMap

class AkkademyDB extends Actor
{
  val map = new HashMap[String,Object]
  val log = Logging(context.system, this)
  override def receive() = 
  {
    case SetRequest(key, value) =>
        log.info("Received SetRequest - key: {} value {}", key, value)
        map.put(key, value)
	sender() ! Status.Success
    case GetRequest(key) =>
	log.info("received GetRequest - key: {}", key)
	val response: Option[String] = map.get(key)
	response match
		{
			case Some(x) => sender() ! x
			case None => sender() ! Status.Failure(new KeyNotFoundException(key))
		}
    case o => Status.Failure(new ClassNotFoundException)
  }
}

object Main extends App {
	val system = ActorSystem("akkademy")
	system.actorOf(Props[AkkademyDb], name = "akkademy-db")
}
