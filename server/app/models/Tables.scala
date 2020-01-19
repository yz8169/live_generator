package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Account.schema ++ Record.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Account
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param account Database column account SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(VARCHAR), Length(255,true)
   *  @param email Database column email SqlType(VARCHAR), Length(255,true) */
  case class AccountRow(id: Int, account: String, password: String, email: String)
  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
  implicit def GetResultAccountRow(implicit e0: GR[Int], e1: GR[String]): GR[AccountRow] = GR{
    prs => import prs._
    AccountRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table account. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends profile.api.Table[AccountRow](_tableTag, Some("live_generator"), "account") {
    def * = (id, account, password, email) <> (AccountRow.tupled, AccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(account), Rep.Some(password), Rep.Some(email)).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column account SqlType(VARCHAR), Length(255,true) */
    val account: Rep[String] = column[String]("account", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column email SqlType(VARCHAR), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))

  /** Entity class storing rows of table Record
   *  @param macAddress Database column mac_address SqlType(VARCHAR), PrimaryKey, Length(255,true)
   *  @param unit Database column unit SqlType(TEXT)
   *  @param software Database column software SqlType(VARCHAR), Length(255,true)
   *  @param ip Database column ip SqlType(VARCHAR), Length(255,true)
   *  @param time Database column time SqlType(DATETIME) */
  case class RecordRow(macAddress: String, unit: String, software: String, ip: String, time: DateTime)
  /** GetResult implicit for fetching RecordRow objects using plain SQL queries */
  implicit def GetResultRecordRow(implicit e0: GR[String], e1: GR[DateTime]): GR[RecordRow] = GR{
    prs => import prs._
    RecordRow.tupled((<<[String], <<[String], <<[String], <<[String], <<[DateTime]))
  }
  /** Table description of table record. Objects of this class serve as prototypes for rows in queries. */
  class Record(_tableTag: Tag) extends profile.api.Table[RecordRow](_tableTag, Some("live_generator"), "record") {
    def * = (macAddress, unit, software, ip, time) <> (RecordRow.tupled, RecordRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(macAddress), Rep.Some(unit), Rep.Some(software), Rep.Some(ip), Rep.Some(time)).shaped.<>({r=>import r._; _1.map(_=> RecordRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column mac_address SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val macAddress: Rep[String] = column[String]("mac_address", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column unit SqlType(TEXT) */
    val unit: Rep[String] = column[String]("unit")
    /** Database column software SqlType(VARCHAR), Length(255,true) */
    val software: Rep[String] = column[String]("software", O.Length(255,varying=true))
    /** Database column ip SqlType(VARCHAR), Length(255,true) */
    val ip: Rep[String] = column[String]("ip", O.Length(255,varying=true))
    /** Database column time SqlType(DATETIME) */
    val time: Rep[DateTime] = column[DateTime]("time")
  }
  /** Collection-like TableQuery object for table Record */
  lazy val Record = new TableQuery(tag => new Record(tag))
}
