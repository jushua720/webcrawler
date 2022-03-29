package services

import dto.WebPage
import cats.effect.IO
import cats.effect.std.CountDownLatch
import org.jsoup.Jsoup
import utils.Debugger
import cats.syntax.parallel._

import scala.collection.mutable

object CrawlerService {

  sealed trait Service {
    val content: mutable.Map[Int, WebPage] = scala.collection.mutable.Map[Int, WebPage]()
    var id = 0

    def countChunks(urls: Array[String]): IO[Int]

    def getChunk(
        chunk: Int,
        urls: Array[String]): IO[String]

    def createTask(
        id: Int,
        latch: CountDownLatch[IO], urls: Array[String]): IO[Unit]

    def fetch(url: String): IO[String]
    def update(url: String, title: String): IO[Unit]

    def crawl(urls: Array[String]): IO[scala.collection.mutable.Map[Int, WebPage]]

  }

  object ServiceImpl extends Service {
    def countChunks(urls: Array[String]): IO[Int] = IO(urls.length)

    def getChunk(chunk: Int, urls: Array[String]): IO[String] = IO(urls(chunk))

    def fetch(url: String): IO[String] =
      IO(Jsoup
          .connect(url)
          .get()
          .title()
      ).redeemWith(
        e => IO(s"FAIL: $e"),
        ioa => IO(s"$ioa"))

    def update(url: String, title: String): IO[Unit] = {
      id += 1
      content.put(id, WebPage(url, title))

      IO.print(s"update")
    }

    def createTask(
        id: Int,
        latch: CountDownLatch[IO],
        urls: Array[String]): IO[Unit] =
      for {
        _ <- IO(s"[task $id] process chunk").debug
        url <- getChunk(id, urls)
        title <- fetch(url)
        _ <- update(url, title)
        _ <- IO(s"[task $id] task complete").debug
        _ <- latch.release
      } yield ()

    def crawl(
        urls: Array[String]): IO[mutable.Map[Int, WebPage]] =
      for {
        n <- countChunks(urls)
        latch <- CountDownLatch[IO](n)
        _ <- (0 until n).toList.parTraverse(id => createTask(id, latch, urls))
        _ <- latch.await
        _ <- IO.print(content).debug
      } yield content
  }

}
