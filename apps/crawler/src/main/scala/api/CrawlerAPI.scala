package api

import cats.effect.IO
import dto.Urls
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder, Header, HttpRoutes}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.io._
import io.circe.generic.auto._
import org.typelevel.ci.CIString
import services.CrawlerService.ServiceImpl
import utils.Debugger



class CrawlerAPI {
  type CrawlerTask[A] = IO[A]

  implicit def jsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[CrawlerTask, A] = jsonOf[CrawlerTask, A]
  implicit def jsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[CrawlerTask, A] = jsonEncoderOf[CrawlerTask, A]


  val routes = HttpRoutes.of[CrawlerTask] {
    case req @ POST -> Root => ( for {
      record <- req.as[Urls]
      _ <- IO.print(record).debug
      titles <- ServiceImpl.crawl(record.url)
    } yield titles).flatMap(
      Ok(_,
        Header.Raw(CIString("Access-Control-Allow-Origin"), "*")
      )
    )
    case _ => Ok("Success")
  }
}
