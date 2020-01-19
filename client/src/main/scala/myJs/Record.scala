package myJs

import com.karasiq.bootstrap.Bootstrap.default._
import myJs.Utils._
import myJs.myPkg._
import org.querki.jquery._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import myPkg.Implicits._


/**
  * Created by yz on 2019/4/25
  */
@JSExportTopLevel("Record")
object Record {

  @JSExport("init")
  def init = {
    $("#table").bootstrapTable()
    refreshTable()

  }

  @JSExport("refreshTable")
  def refreshTable(f: () => js.Any = () => ()) = {
    val url = g.jsRoutes.controllers.AdminController.getAllRecord().url.toString
    val ajaxSettings = JQueryAjaxSettings.url(s"${url}?").contentType("application/json").
      `type`("get").success { (data, status, e) =>
      $("#table").bootstrapTable("load", data)
      f()
    }
    $.ajax(ajaxSettings)

  }

  def check(mac: String): Boolean = {
    var b=true
    val lines = mac.split("-")
    if (lines.length != 6) b=false
    for (line <- lines) {
      if (line.length != 2) b=false
    }
    if(!b){
      Swal.swal(SwalOptions.title("错误").text("输入的mac地址不合法！").`type`("error"))
    }
    b
  }

  @JSExport("bootStrapValidator")
  def bootStrapValidator = {
    val dict = js.Dictionary(
      "feedbackIcons" -> js.Dictionary(
        "valid" -> "glyphicon glyphicon-ok",
        "invalid" -> "glyphicon glyphicon-remove",
        "validating" -> "glyphicon glyphicon-refresh",
      ),
      "fields" -> js.Dictionary(
        "macAddress" -> js.Dictionary(
          "validators" -> js.Dictionary(
            "notEmpty" -> js.Dictionary(
              "message" -> "MAC地址不能为空！",
            )
          )
        ),
      )
    )
    g.$(s"#form").bootstrapValidator(dict)

  }


}
