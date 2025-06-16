package ru.samsmu.app.ui

import ru.samsmu.app.data.model.User

interface Fetchable {
    fun fetch(callback: (Collection<User>) -> Unit)
}