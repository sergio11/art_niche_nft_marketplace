package com.dreamsoftware.artcollectibles.data.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "token_metadata")
data class TokenMetadataEntity(
    @PrimaryKey
    @ColumnInfo(name = "cid")
    val cid: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "tags")
    val tags: List<String>,
    @ColumnInfo(name = "category")
    val categoryUid: String,
    @ColumnInfo(name = "author_address")
    val authorAddress: String,
    @ColumnInfo(name = "device_name")
    val deviceName: String? = null,
)
