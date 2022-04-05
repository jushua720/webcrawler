import api.CrawlerAPI
import org.http4s.server.Router
import configuration._
import org.http4s.blaze.server.BlazeServerBuilder
import cats.effect.IO

object Server {

  val httpApp = Router("/v1/crawler" -> (new CrawlerAPI).routes).orNotFound

  val program = for {
    config <- Configuration.load()
    server <- BlazeServerBuilder[IO]
      .bindHttp(config.port, config.host)
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
  } yield server
}
