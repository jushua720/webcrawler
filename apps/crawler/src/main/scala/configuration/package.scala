import cats.effect.IO
import io.circe.config.parser
import io.circe.generic.auto._

package object configuration {
  final case class ApiConfig(host: String, port: Int)

  object Configuration {
    def load(): IO[ApiConfig] = parser.decodePathF[IO, ApiConfig]("server")
  }
}
