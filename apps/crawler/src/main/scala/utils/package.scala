import cats.effect.IO

package object utils {
  implicit class Debugger[A](io: IO[A]) {
    def debug: IO[A] = for {
      ioa <- io
      thread = Thread.currentThread().getName
      _ = println(s"[$thread] $ioa")
    } yield ioa
  }
}
