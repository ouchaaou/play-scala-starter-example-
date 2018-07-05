package database
import doobie._
import doobie.implicits._


import cats.effect._
import cats.effect.IO



import models._

object Connexion {


// A transactor that gets connections from java.sql.DriverManager
val xa = Transactor.fromDriverManager[IO](
  "org.postgresql.Driver", // driver classname
  "jdbc:postgresql:play", // connect URL (driver-specific)
  "play",              // user
  "play"               // password
)
  //val program2 = sql"select ID FROM company".query[Int].unique
  //val io2 = program2.transact(xa)

  def listCompany: IO[List[Company]] = {
      sql"select * from company"
        .query[(Int,String, Int , Option[String], Option[Double])]
        .to[List]
        .transact(xa).map(_.map(data => (Company.apply1 _).tupled(data)))

  }


 // Example for Tuple
  def listDepartment: IO[List[(Int,String,Int)]] = {
    sql"SELECT * FROM department"
      .query[(Int,String,Int)]
      .to[List]
      .transact(xa)
  }


  // Insert Company
  def insertCompany(comp:Company): IO[Int] =
    sql"insert into company (id, name,age, address ,salary) values (${comp.id}, ${comp.name}, ${comp.age}, ${comp.addresse}, ${comp.salary})".update.run
      .transact(xa)

  // Insert Department
  def insertDepartment(de:Department): IO[Int] =
    sql"insert into department (id, dept, emp_id)           values (${de.id}, ${de.dept}, ${de.emp_id})".update.run
    .transact(xa)

  // Select Departement by ID
  def selectDepartment(id:Int): IO[List[Department]] =
    sql"select * FROM department WHERE emp_id =$id"
      .query[Department]
      .to[List]
      .transact(xa)


  def listCompanyWithlistDepartment:IO[List[Company]] = {
    listCompany.map(lcompany => lcompany.map(x => {
      val deps = selectDepartment(x.id).unsafeRunSync()
      val emp  = selectEmploye(x.id).unsafeRunSync()
      x.copy(departments = deps,     employeS = emp)
    }))
  }

  def selectEmploye(id:Int): IO[List[Employe]] =
    sql"select * FROM employe WHERE com_id =$id"
      .query[Employe]
      .to[List]
      .transact(xa)

}


