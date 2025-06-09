package ru.samsmu.app.ui

import ru.samsmu.app.data.model.User

interface ReloadableList<T> {

    fun reloadUsers(dataset: List<T>?)

}