package com.dreamsoftware.artcollectibles.domain.models

data class UserInfo(
    val uid: String,
    val name: String,
    val info: String? = null,
    val contact: String,
    val photoUrl: String? = null,
    val walletAddress: String
)
