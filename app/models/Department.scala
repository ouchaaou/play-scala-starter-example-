
package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Department(id:Int, dept:String, emp_id:Int)

object Department{
  implicit val departmentReads: Reads[Department] = (
      (JsPath \ "id").read[Int] and
      (JsPath \ "dept").read[String] and
      (JsPath \ "emp_id").read[Int]
      )(Department.apply _)
}
