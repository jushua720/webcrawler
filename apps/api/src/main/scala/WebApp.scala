import configs._

object WebApp extends App {
  Server
    .start(
      ServerConfig(),
      RouteConfig()
    )
}
