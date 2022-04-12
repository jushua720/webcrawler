import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    Server
      .program
      .as(ExitCode.Success)
  }
}
