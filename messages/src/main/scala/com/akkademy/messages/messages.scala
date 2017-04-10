package com.akkademy.messages

import akka.actor.ActorRef

trait Request
case class SetRequest(
  key: String,
  value: Object,
  sender: ActorRef = ActorRef.noSender) extends Request
case class GetRequest(
  key: String,
  sender: ActorRef = ActorRef.noSender) extends Request

case class KeyNotFoundException(key: String) extends Exception

case class Connected()
case class Disconnected()

case class Flush()

case class ParseArticle(url: String)
case class HttpResponse(htmlString: String)
case class ParseHtmlArticle(url: String, htmlString: String)
case class ArticleBody(url: String, body: String)
