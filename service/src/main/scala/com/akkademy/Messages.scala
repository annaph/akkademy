package com.akkademy

case class ParseArticle(url: String)
case class HttpResponse(htmlString: String)
case class ParseHtmlArticle(url: String, htmlString: String)
case class ArticleBody(url: String, body: String)
