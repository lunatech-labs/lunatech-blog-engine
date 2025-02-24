package integration

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

class SmokeTest extends PlaySpec with GuiceOneServerPerSuite {

  override def fakeApplication(): Application = {
    GuiceApplicationBuilder()
      .configure(
        "accessToken" -> sys.env.getOrElse("GITHUB_TOKEN", "dummy")
      )
      .build()
  }

  "should return some page" in {
    val wsClient              = app.injector.instanceOf[WSClient]
    val response = await(wsClient.url(s"http://localhost:$port/").withVirtualHost("blog.lunatech.com").get())
    response.status mustBe OK
  }
}
