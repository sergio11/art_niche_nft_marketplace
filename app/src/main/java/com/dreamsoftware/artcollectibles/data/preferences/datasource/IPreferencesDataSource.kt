package com.dreamsoftware.artcollectibles.data.preferences.datasource


interface IPreferencesDataSource {

    suspend fun saveAuthUserUid(uid: String)

    suspend fun getAuthUserUid(): String

    suspend fun clearData()
}