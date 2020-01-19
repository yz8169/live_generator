package controllers

import dao._
import javax.inject.Inject
import models.Tables.RecordRow
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._
import shared._
import tool.{DesEncrypter, FormTool}
import utils.Utils

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, TimeoutException}
import scala.util.{Failure, Success}

/**
  * Created by yz on 2018/8/20
  */
class AdminController @Inject()(cc: ControllerComponents, formTool: FormTool, ws: WSClient,
                                accountDao: AccountDao, recordDao: RecordDao) extends AbstractController(cc) {

  def boostBefore = Action { implicit request =>
    Ok(views.html.admin.boost())

  }

  def logout = Action { implicit request =>
    Redirect(routes.AppController.loginBefore()).flashing("info" -> "退出登录成功!", "class" -> Utils.successClass).
      removingFromSession(Shared.adminStr)
  }

  def generate = Action.async { implicit request =>
    val data = formTool.licenseForm.bindFromRequest().get
    val key = data.kind match {
      case "boost" => "Huiyun_LiveBoost"
      case "forest" => "HuiyunLiveForest"
      case "fat" => "Huiyun_LiveFatV1"
    }
    val software = data.kind match {
      case "boost" => "Live Boost"
      case "forest" => "Live Forest"
      case "fat" => "Live Fat"
    }
    val macAddress = data.macAddress
    val desEncrypter = new DesEncrypter(key)
    val license = desEncrypter.encrypt(data.macAddress)
    val ip = request.remoteAddress
    val unit = data.unit

    val cityOpF = ws.url("http://ip.taobao.com/service/getIpInfo.php").
      addQueryStringParameters("ip" -> ip).
      withRequestTimeout(1 seconds).get().map { resonse =>
      resonse.status match {
        case 200 => (resonse.json \ "data" \ "city").asOpt[String]
        case _ => None
      }
    }.recover {
      case e: Exception =>
        e.printStackTrace()
        None
    }

    cityOpF.flatMap {
      cityOp =>
        val ipInfo = cityOp match {
          case Some(city) => s"${ip}(${city})"
          case None => ip
        }
        recordDao.selectByMac(data.macAddress).flatMap { recordOp =>
          val time = new DateTime()
          val newRow = recordOp match {
            case Some(record) =>
              val dbSoftware = record.software
              val newSoftware = if (dbSoftware.contains(software)) dbSoftware else s"${dbSoftware},${software}"
              record.copy(software = newSoftware, ip = ipInfo, time = time, unit = unit)
            case None =>
              RecordRow(macAddress, unit = unit, software, ipInfo, time)
          }
          recordDao.insertOrUpdate(newRow).map { x =>
            Ok(Json.toJson(license))
          }
        }
    }

  }

  def recordBefore = Action { implicit request =>
    Ok(views.html.admin.record())
  }

  def getAllRecord = Action.async { implicit request =>
    recordDao.selectAll.map { x =>
      val array = Utils.getArrayByTs(x)
      Ok(Json.toJson(array))
    }

  }

  def changePasswordBefore = Action { implicit request =>
    Ok(views.html.admin.changePassword())

  }

  def changePassword = Action.async { implicit request =>
    val data = formTool.changePasswordForm.bindFromRequest().get
    accountDao.selectById1.flatMap { x =>
      if (data.password == x.password) {
        accountDao.updatePassword(data.newPassword).map { y =>
          Redirect(routes.AppController.loginBefore()).flashing("info" -> "密码修改成功!", "class" -> Utils.successClass).
            removingFromSession(Shared.adminStr)
        }
      } else {
        Future.successful(Redirect(routes.AdminController.changePasswordBefore()).flashing("info" -> "旧密码错误!", "class" -> Utils.errorClass))
      }
    }
  }


}
