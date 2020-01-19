package myJs

import org.querki.jquery._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import com.karasiq.bootstrap.Bootstrap.default._
import Utils._

import scala.scalajs.js
import myPkg._
import org.scalajs.dom._


/**
  * Created by yz on 2019/4/25
  */
@JSExportTopLevel("Generator")
object Generator {

  @JSExport("init")
  def init = {
    bootStrapValidator

  }

  @JSExport("generate")
  def generate = {
    $("#content").hide()
    val bv = jQuery(s"#form").data("bootstrapValidator")
    bv.validate()
    val valid = bv.isValid().asInstanceOf[Boolean]
    val macAddress = $(":input[name='macAddress']").`val`().toString
    if (valid && check(macAddress)) {
      val data = $(s"#form").serialize()
      val url = g.jsRoutes.controllers.AdminController.generate().url.toString
      val index = layer.alert(element, layerOptions)
      val ajaxSettings = JQueryAjaxSettings.url(url).
        `type`("post").data(data).success { (data, status, e) =>
        $(":input[name='license']").`val`(data.toString)
        $("#content").show()
        layer.close(index)
      }
      $.ajax(ajaxSettings)

    }


  }

  @JSExport("copy")
  def copy = {
    $(":input[name='license']").select()
    document.execCommand("Copy")
    window.alert("复制成功！")

  }

  def check(mac: String): Boolean = {
    var b = true
    val lines = mac.split("-")
    if (lines.length != 6) b = false
    for (line <- lines) {
      if (line.length != 2) b = false
    }
    if (!b) {
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
        "unit" -> js.Dictionary(
          "validators" -> js.Dictionary(
            "notEmpty" -> js.Dictionary(
              "message" -> "单位名称不能为空！",
            )
          )
        ),
      )
    )
    g.$(s"#form").bootstrapValidator(dict)

  }


}
