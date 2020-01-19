package myJs

import org.querki.jquery._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Created by yz on 2019/4/25
  */
@JSExportTopLevel("App")
object App {

  @JSExport("init")
  def init={
    val shareTitle="Live系列软件 序列生成器"
    val beforeTitle=$("title").text()
    $("title").text(s"${beforeTitle}-${shareTitle}")

  }

}
