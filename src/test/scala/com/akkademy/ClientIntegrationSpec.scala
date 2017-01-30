package com.akkademy

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

class ClientIntegrationSpec extends FunSpecLike with Matchers {
  val client = new Client("127.0.0.1:44055")

  describe("akkademyDbClient") {
    it("should set a vlaue") {
      client.set("123", new Integer(123))
      val f = client.get("123")
      val r = Await.result(f.mapTo[Int], 30 seconds)
      r should equal(123)
    }
  }

}
