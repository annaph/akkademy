package com.akkademy

import com.akkademy.messages.ArticleBody
import com.akkademy.messages.ParseHtmlArticle
import com.akkademy.messages.ParseHtmlArticleAsk

import akka.actor.Actor
import de.l3s.boilerpipe.extractors.ArticleExtractor

class ArticleParsingActor extends Actor {

  override def receive = {
    case ParseHtmlArticle(url, htmlString, senderRef) =>
      senderRef ! ArticleBody(url, parse(htmlString))
    case ParseHtmlArticleAsk(url, htmlString) =>
      sender() ! ArticleBody(url, parse(htmlString))
    case x =>
      println("unknown message " + x.getClass)
  }

  private def parse(htmlString: String): String =
    ArticleExtractor.INSTANCE.getText(htmlString)

}
