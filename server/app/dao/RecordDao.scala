package dao

import javax.inject.Inject
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RecordDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._

  def selectAll= db.run(Record.sortBy(_.time.desc).result)

  def selectByMac(mac:String)= db.run(Record.
    filter(_.macAddress===mac).result.headOption)

  def update(row: RecordRow): Future[Unit] = {
    db.run(Record.filter(_.macAddress === row.macAddress).update(row)).map(_ => ())
  }

  def insertOrUpdate(row: RecordRow) = db.run(Record.
    insertOrUpdate(row)).map(_ => ())



}
