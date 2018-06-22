package database
import doobie._
import doobie.implicits._

import cats._
import cats.effect._
import cats.implicits._



object Connexion {


// A transactor that gets connections from java.sql.DriverManager
val xa = Transactor.fromDriverManager[IO](
  "org.postgresql.Driver", // driver classname
  "jdbc:postgresql:play", // connect URL (driver-specific)
  "playexample",              // user
  "playexample"               // password
)
  val program2 = sql"select ID FROM company".query[Int].unique
  val io2 = program2.transact(xa)

}


