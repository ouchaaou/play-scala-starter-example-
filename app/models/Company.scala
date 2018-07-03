
package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


case class Company(id:Int, name:String, age:Int ,addresse: Option[String], salary: Option[Double])


object Company{
  implicit val companyReads: Reads[Company] = (
(JsPath \ "id").read[Int] and
(JsPath \ "name").read[String] and
(JsPath \ "age").read[Int] and
(JsPath \ "addresse").readNullable[String] and
(JsPath \ "salary").readNullable[Double]
)(Company.apply _)
}


