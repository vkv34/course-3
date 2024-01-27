package ru.online.education.di

import kotlinx.coroutines.Dispatchers
import model.User
import model.UserRole
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import ru.online.education.data.table.*

val dataBaseModule = module(createdAtStart = true) {
    single<Database> {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val jdbcURL = "jdbc:mysql://45.146.164.243:3306/online_education"
        val database = Database.connect(
            jdbcURL,
            driver = driverClassName,
            user = "root",
            password = "password"
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
                UserSessionTable
            )
        }

        fun <T : Any> UsersTable.userToInsertStatement(statement: InsertStatement<T>, user: User) {
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
                        User(
                            email = "email",
                            password = "password",
                            firstName = "admin",
                            secondName = "admin",
                            lastName = "admin",
                            role = UserRole.Admin
                        )
                    )
                }
            }
        }

        database
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }