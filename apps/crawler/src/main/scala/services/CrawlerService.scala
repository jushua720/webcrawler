package services

import dto.WebPage
import cats.effect.IO
import cats.effect.std.CountDownLatch
import org.jsoup.Jsoup
import utils.Debugger
import cats.syntax.parallel._

import scala.collection.mutable

object CrawlerService {

  sealed trait Params {
    val content: mutable.Map[Int, WebPage] = scala.collection.mutable.Map[Int, WebPage]()
    val regex = "\\A(FAIL:)(.*)".r
    var id = 0
  }

  sealed trait Service {
    protected def countChunks(urls: Array[String]): IO[Int]

    protected def getChunk(
        chunk: Int,
        urls: Array[String]): IO[String]

    protected def createTask(
        id: Int,
        latch: CountDownLatch[IO], urls: Array[String]): IO[Unit]

    protected def fetch(url: String): IO[String]
    protected def update(url: String, title: String): IO[Unit]

    def crawl(urls: Array[String]): IO[scala.collection.mutable.Map[Int, WebPage]]

  }

  object ServiceImpl extends Service with Params {
    override protected def countChunks(urls: Array[String]): IO[Int] = IO(urls.length)

    override protected def getChunk(chunk: Int, urls: Array[String]): IO[String] = IO(urls(chunk))

    override protected def fetch(url: String): IO[String] =
      IO(Jsoup
        .connect(url)
        .get()
        .title()
      ).redeemWith(
        e => IO(s"FAIL: $e"),
        ioa => IO(s"$ioa"))

    override protected def update(url: String, title: String): IO[Unit] = {
      id += 1

      title match {
        case regex(error, msg) =>  content.put(id, WebPage(url, "", s"$error $msg"))
        case _ =>  content.put(id, WebPage(url, title, ""))
      }

      IO.print(s"update")
    }

    override protected def createTask(
      id: Int,
      latch: CountDownLatch[IO],
      urls: Array[String]
    ): IO[Unit] = for {
        _ <- IO(s"[task $id] process chunk").debug
        url <- getChunk(id, urls)
        title <- fetch(url)
        _ <- update(url, title)
        _ <- IO(s"[task $id] task complete").debug
        _ <- latch.release
      } yield ()

    def crawl(urls: Array[String]): IO[mutable.Map[Int, WebPage]] =
      for {
        n <- countChunks(urls)
        latch <- CountDownLatch[IO](n)
        _ <- (0 until n).toList.parTraverse(id => createTask(id, latch, urls))
        _ <- latch.await
        _ <- IO.print(content).debug
      } yield content
  }

}
