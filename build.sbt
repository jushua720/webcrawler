lazy val _version = "0.1"
lazy val _scalaVersion = "2.13.8"
lazy val _catsVersion = "2.7.0"
lazy val _catsEffectVersion = "3.2.0"
lazy val _circeVersion = "0.14.1"
lazy val _http4sVersion = "0.23.11"
lazy val _circeConfigVersion = "0.8.0"
lazy val _jsoupVersion = "1.14.1"

lazy val _akkaHttpVersion = "10.2.9"
lazy val _akkaVersion = "2.6.19"


lazy val _idePackagePrefix = Some("ru.coding.test.webcrawler")


lazy val crawler = (project in file("apps/crawler"))
  .settings(
    name := "coding.test.webcrawler.apps.crawler",
    version := _version,
    scalaVersion := _scalaVersion,


    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % _catsEffectVersion,
      "org.typelevel" %% "cats-core" % _catsVersion,

      "io.circe" %% "circe-core" % _circeVersion,
      "io.circe" %% "circe-generic" % _circeVersion,
      "io.circe" %% "circe-parser" % _circeVersion,
      "io.circe" %% "circe-config" % _circeConfigVersion,

      "org.http4s" %% "http4s-blaze-server"  % _http4sVersion,
      "org.http4s" %% "http4s-circe"         % _http4sVersion,
      "org.http4s" %% "http4s-dsl"           % _http4sVersion,
      "org.http4s" %% "http4s-blaze-client"  % _http4sVersion,

      "org.jsoup" % "jsoup" % _jsoupVersion,
    ),
  )


lazy val webApi = (project in file("apps/api"))
  .settings(
    name := "coding.test.webcrawler.apps.api",
    version := _version,
    scalaVersion := _scalaVersion,


    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % _catsEffectVersion,

      "com.typesafe.akka" %% "akka-remote" % _akkaVersion,
      "com.typesafe.akka" %% "akka-http" % _akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % _akkaHttpVersion,
    ),
  )
