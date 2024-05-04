package ru.online.education.di

import kotlinx.coroutines.Dispatchers
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.model.UserRole
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import ru.online.education.data.table.*
import java.sql.DriverManager
import java.sql.SQLException
import java.util.logging.Logger

fun connectDatabase(
    jdbcUrl: String,
    database: String,
    user: String,
    password: String,
): Database {
    val driverClassName = "com.mysql.cj.jdbc.Driver"
    try {
        val connection = DriverManager.getConnection(jdbcUrl, user, password)
//        val resultSet = connection.createStatement().executeQuery("SHOW DATABASES LIKE '$database'")
        connection.createStatement()
            .execute(
                """
                create database if not exists $database;
                """.trimIndent(),
            )
        Logger.getLogger("adsad").severe("Database created")
    } catch (e: SQLException) {
        e.printStackTrace()
        Logger.getLogger("adsad").severe("Not created")
        Logger.getLogger("adsad").severe(e.message)
    }
    return Database.connect(
        jdbcUrl + database,
        driver = driverClassName,
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD"),
    )
}

val dataBaseModule =
    module(createdAtStart = true) {
        single<Database> {
            val driverClassName = "com.mysql.cj.jdbc.Driver"
            val jdbcURL = System.getenv("CONNECTION_STRING")
            val databaseName = System.getenv("DATABASE")

            val database =
                connectDatabase(
                    jdbcURL,
                    databaseName,
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD"),
                )

            transaction(database) {
                SchemaUtils.create(
                    UserRoleTable,
                    UsersTable,
                    CourseCategoryTable,
                    CourseTable,
                    UserOnCourse,
                    PublicationTable,
                    PublicationAttachmentTable,
                    PublicationOnCourse,
                    UserSessionTable,
                    PublicationAnswerTable,
                    PublicationAnswerAttachmentTable
                )
            }

            fun <T : Any> UsersTable.userToInsertStatement(
                statement: InsertStatement<T>,
                user: UserDto,
            ) {
                val roleId = EntityID(user.role.ordinal.inc(), UserRoleTable)
                statement[email] = user.email
                statement[password] = user.password
                statement[firstName] = user.firstName
                statement[secondName] = user.secondName
                statement[lastName] = user.lastName
                statement[userRole] = roleId
            }

            transaction(database) {
                if (UserRoleTable.selectAll().empty()) {
                    UserRole.entries.forEach { userRole: UserRole ->
                        UserRoleTable.insert {
                            it[name] = userRole.name
                        }
                    }
                }
                if (UsersTable.selectAll().limit(10).empty()) {
                    UsersTable.insert {
                        userToInsertStatement(
                            it,
                            UserDto(
                                email = "admin1",
                                password = "password",
                                firstName = "Преподователь1",
                                secondName = "Иван",
                                lastName = "Иванович",
                                role = UserRole.Admin,
                            ),
                        )
                    }
                    UsersTable.insert {
                        userToInsertStatement(
                            it,
                            UserDto(
                                email = "admin2",
                                password = "password",
                                firstName = "Преподаватель2",
                                secondName = "Петр",
                                lastName = "Петрович",
                                role = UserRole.Admin,
                            ),
                        )
                    }
                    UsersTable.insert {
                        userToInsertStatement(
                            it,
                            UserDto(
                                email = "student1",
                                password = "password",
                                firstName = "Слушатель1",
                                secondName = "admin",
                                lastName = "admin",
                                role = UserRole.Student,
                            ),
                        )
                    }
                    UsersTable.insert {
                        userToInsertStatement(
                            it,
                            UserDto(
                                email = "student2",
                                password = "password",
                                firstName = "Слушатель2",
                                secondName = "admin",
                                lastName = "admin",
                                role = UserRole.Student,
                            ),
                        )
                    }

                    exec("""create fulltext index course_category_name_index on CourseCategory(name)""")
                }
            }

            database
        }
    }

suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
