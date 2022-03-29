import sbt.Keys.resolvers

lazy val _version = "0.1"
lazy val _scalaVersion = "2.13.8"
lazy val _catsVersion = "2.7.0"
lazy val _catsEffectVersion = "3.2.0"
lazy val _circeVersion = "0.14.1"
lazy val _http4sVersion = "0.23.11"
lazy val _circeConfigVersion = "0.8.0"
lazy val _jsoupVersion = "1.14.1"


lazy val _idePackagePrefix = Some("ru.coding.test.webcrawler")


lazy val root = (project in file("."))
  .settings(
    name := "coding.test.webcrawler",
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


