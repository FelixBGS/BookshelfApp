package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class Items(
    val items: List<Item>
)

@Serializable
data class Item(
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val description: String? = null,
    val authors: List<String>? = null,
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)