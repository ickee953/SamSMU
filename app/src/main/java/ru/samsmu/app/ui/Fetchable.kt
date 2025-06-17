package ru.samsmu.app.ui

import ru.samsmu.app.data.model.User

interface Fetchable {
    fun fetch(
        success: (List<User>) -> Unit,
        error: (String?) -> Unit = {},
        loading: () -> Unit = {}
    )
}