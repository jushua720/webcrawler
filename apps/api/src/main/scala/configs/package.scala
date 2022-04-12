package object configs {
  case class RouteConfig(
      basePath: String = "apps/api/src/main/web",
      basePathPrefix: String = "",
      assetsPathPrefix: String = "assets",
      file: String = "index.html",
  )

  case class ServerConfig(
      host: String = "localhost",
      port: Int = 9988,
  )

}
