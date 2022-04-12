import akka.actor.ActorSystem
import configs._
import akka.http.scaladsl.Http
import cats.effect.IO

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

object Server {

  implicit val system: ActorSystem = ActorSystem()

  private def route(config: RouteConfig): Route = {
    (pathEndOrSingleSlash &
      redirectToTrailingSlashIfMissing(StatusCodes.TemporaryRedirect)) {
      getFromFile(s"${config.basePath}/${config.file}")

    } ~ pathPrefix(config.basePathPrefix) {
      getFromDirectory(s"${config.basePath}/")

    } ~ pathPrefix(config.assetsPathPrefix) {
      getFromDirectory(s"${config.basePath}/${config.assetsPathPrefix}/")

    }
  }

  def start(serverConfig: ServerConfig, routeConfig: RouteConfig): IO[Unit] = {
    Http().newServerAt(
      serverConfig.host,
      serverConfig.port)
      .bindFlow(
        route(routeConfig)
      )

    IO.print(s"server start")
  }
}
