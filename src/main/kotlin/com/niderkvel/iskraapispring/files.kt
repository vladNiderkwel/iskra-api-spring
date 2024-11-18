package com.niderkvel.iskraapispring

import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

private val SAVE_DIR = "${System.getProperty("user.dir")}/src/main/images"

fun saveFile(
    fileName: String?,
    file: MultipartFile,
    type: Byte
) {
    val folder = when (type) {
        PHOTO_TYPE_PHOTOS -> "photos"
        PHOTO_TYPE_POSTS -> "posts"
        else -> ""
    }

    val path = Paths.get("$SAVE_DIR/$folder/", "$fileName.jpg")
    Files.write(path, file.bytes)
}

fun deleteFile(
    fileName: String,
    type: Byte
) {
    val folder = when (type) {
        PHOTO_TYPE_PHOTOS -> "photos"
        PHOTO_TYPE_POSTS -> "posts"
        else -> ""
    }

    val path = Paths.get("$SAVE_DIR/$folder/", "$fileName.jpg")
    Files.delete(path)
}