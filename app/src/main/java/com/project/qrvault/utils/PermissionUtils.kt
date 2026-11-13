package com.project.qrvault.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtils {

    const val READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS
    const val REQUEST_CODE_READ_CONTACTS = 101

    fun hasReadContactsPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            READ_CONTACTS_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
