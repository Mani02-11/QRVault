package com.project.qrvault.model

import android.net.Uri
import java.io.Serializable

data class ContactModel(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val photoUri: Uri? = null,
    var isSelected: Boolean = false
) : Serializable
