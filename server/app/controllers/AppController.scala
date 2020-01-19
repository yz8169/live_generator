package controllers

import dao._
import javax.inject.Inject
import org.joda.time.DateTime
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter
import tool.FormTool

import scala.concurrent.ExecutionContext.Implicits.global
import shared._
import utils.Utils

/**
  * Created by yz on 2018/8/20
  */
class AppController @Inject()(cc: ControllerComponents, formTool: FormTool,
                              accountDao: AccountDao) extends AbstractController(cc) {

  def loginBefore = Action { implicit request =>
    Ok(views.html.login())
  }

  def login = Action.async { implicit request =>
    val data = formTool.userForm.bindFromRequest().get
    accountDao.selectById1.map { account =>
      if (data.name == account.account && data.password == account.password) {
        Redirect(routes.AdminController.boostBefore()).addingToSession(Shared.adminStr -> data.name)
      } else {
        Redirect(routes.AppController.loginBefore()).flashing("info" -> "用户名或密码错误!", "class" -> Utils.errorClass)
      }
    }
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        controllers.routes.javascript.AdminController.generate,
        controllers.routes.javascript.AdminController.getAllRecord,




      )
    ).as("text/javascript")
  }


}
