package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

import database.Connexion
import models._


/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  /**
    * Creates an Action that returns a plain text message after a delay
    * of 1 second.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/message`.
    */
  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  //  def count = Action { Ok(counter.nextCount().toString) }

  def hello = Action {
    Ok(Connexion.listCompanyWithlistDepartment.unsafeRunSync().mkString)
  }

  def hellos = Action {
    val departments = Connexion.listDepartment.unsafeRunSync()
    println(departments)
    Ok(departments.mkString)
  }


  import play.api.libs.json.Json

  def createCompany = Action { request =>
    val companys = request.body.asJson.get.as[Company] // Ca transore Json a company
    val changed =  Connexion.insertCompany(companys).unsafeRunSync()
    Ok(changed.toString)
  }


  def createDepartment = Action { request =>
    val dep = request.body.asJson.get.as[Department]
    println(dep)  // juste afficher les vaeurs recu de JSON
    val changedd = Connexion.insertDepartment(dep).unsafeRunSync()
    Ok(changedd.toString)
  }



  def getDepartmentByCompanyId(id:Int) = Action { request =>
    val departments =Connexion.selectDepartment(id).unsafeRunSync()
    Ok(departments.mkString)

  }

}

