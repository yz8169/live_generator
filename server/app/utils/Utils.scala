package utils

import java.lang.reflect.Field

import org.joda.time.DateTime

/**
  * Created by yz on 2019/5/15
  */
object Utils {

  val errorClass = "error"
  val successClass = "text-success"

  val pattern = "yyyy-MM-dd HH:mm:ss"


  def getArrayByTs[T](x: Seq[T]) = {
    x.map { y =>
      getMapByT(y)
    }
  }

  def getMapByT[T](t: T) = {
    t.getClass.getDeclaredFields.toBuffer.map { x: Field =>
      x.setAccessible(true)
      val kind = x.get(t)
      val value = getValue(kind)
      (x.getName, value)
    }.init.toMap
  }

  def getValue[T](kind: T, noneMessage: String = "暂无"): String = {
    kind match {
      case x if x.isInstanceOf[DateTime] => val time = x.asInstanceOf[DateTime]
        time.toString("yyyy-MM-dd HH:mm:ss")
      case x if x.isInstanceOf[Option[T]] => val option = x.asInstanceOf[Option[T]]
        if (option.isDefined) getValue(option.get, noneMessage) else noneMessage
      case _ => kind.toString
    }
  }




}
