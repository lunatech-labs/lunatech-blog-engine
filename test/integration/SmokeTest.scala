
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.ws.WSClient
import org.scalatest._
import org.scalatestplus.play._

import play.api.test._
import play.api.test.Helpers.{GET => GET_REQUEST, _}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

class SmokeTest extends PlaySpec with GuiceOneServerPerSuite {

  override def fakeApplication(): Application = {
    GuiceApplicationBuilder()
      .configure(
        "accessToken" -> sys.env.get("GITHUB_TOKEN").getOrElse("dummy")
      )
      .build()
  }

  "should return some page" in {
    val wsClient              = app.injector.instanceOf[WSClient]
    val response = await(wsClient.url(s"http://localhost:$port/").withVirtualHost("blog.lunatech.com").get())
    response.status mustBe OK
  }
}
