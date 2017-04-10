package com.akkademy

import com.akkademy.messages.ArticleBody
import com.akkademy.messages.ParseHtmlArticle

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
