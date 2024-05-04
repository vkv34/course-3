package ru.online.education.app.feature.account.domain.repository

import kotlinx.browser.document
import ru.online.education.domain.repository.model.AuthRequest
import ru.online.education.domain.repository.model.AuthResponse
import ru.online.education.app.feature.account.domain.model.Auth
import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement

actual fun onAuth(authRequest: AuthRequest) {
    savePassword(with(authRequest) { Auth(email, password) })
}

actual fun onAuthScreenOpened() {
}


fun savePassword(auth: Auth) {
    createAndSubmitForm(auth.email, auth.password)
    console.log("password saved")
}

fun createAndSubmitForm(login: String, password: String) {
    val form = document.createElement("form") as HTMLFormElement
    form.action = "/submit" // Замените на ваш URL
    form.method = "post"

    val loginInput = document.createElement("input") as HTMLInputElement
    loginInput.name = "login"
    loginInput.value = login

    val passwordInput = document.createElement("input") as HTMLInputElement
    passwordInput.name = "password"
    passwordInput.value = password

    val submitButton = document.createElement("button") as HTMLButtonElement
    submitButton.type = "submit"
    submitButton.innerText = "Submit"

    form.appendChild(loginInput)
    form.appendChild(passwordInput)
    form.appendChild(submitButton)

    document.body?.appendChild(form)

    submitButton.click()

    form.remove()
}


