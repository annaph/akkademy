package com.akkademy

import akka.actor.Actor
import de.l3s.boilerpipe.extractors.ArticleExtractor

class ParsingActor extends Actor {

  override def receive = {
    case ParseHtmlArticle(url, htmlString) =>
      sender() ! ArticleBody(url, ArticleExtractor.INSTANCE.getText(htmlString))
    case x =>
      println("unknown message " + x.getClass)
  }

}
