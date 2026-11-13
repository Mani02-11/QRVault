package com.project.qrvault

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.qrvault.database.DatabaseHelper
import com.project.qrvault.model.QRHistoryModel
import com.project.qrvault.utils.ImageUtils
import com.project.qrvault.utils.QRCodeUtils

class QRViewerActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var historyItem: QRHistoryModel? = null
    private var qrBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_viewer)

        val root = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        historyItem = intent.getSerializableExtra("QR_HISTORY_ITEM") as? QRHistoryModel

        if (historyItem == null) {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<ImageButton>(R.id.btnViewBack).setOnClickListener { finish() }

        val btnDelete = findViewById<ImageButton>(R.id.btnViewDelete)
        val tvName = findViewById<TextView>(R.id.tvViewName)
        val tvPhone = findViewById<TextView>(R.id.tvViewPhone)
        val tvDate = findViewById<TextView>(R.id.tvViewDate)
        val tvNameBelow = findViewById<TextView>(R.id.tvViewQrNameBelow)
        val ivQr = findViewById<ImageView>(R.id.ivViewQr)
        val btnSave = findViewById<MaterialButton>(R.id.btnViewSave)
        val btnShare = findViewById<MaterialButton>(R.id.btnViewShare)

        itemToUi(historyItem!!, tvName, tvPhone, tvDate, tvNameBelow, ivQr)

        btnDelete.setOnClickListener {
            confirmDelete()
        }

        btnSave.setOnClickListener {
            val bitmap = qrBitmap ?: return@setOnClickListener
            val fileName = historyItem!!.contactName.replace(" ", "_")
            val uri = ImageUtils.saveBitmapToGallery(this, bitmap, fileName)
            if (uri != null) {
                showSuccessDialog()
            } else {
                Toast.makeText(this, R.string.error_saving_qr, Toast.LENGTH_SHORT).show()
            }
        }

        btnShare.setOnClickListener {
            val bitmap = qrBitmap ?: return@setOnClickListener
            ImageUtils.shareBitmap(this, bitmap, historyItem!!.contactName)
        }
    }

    private fun itemToUi(
        item: QRHistoryModel,
        tvName: TextView,
        tvPhone: TextView,
        tvDate: TextView,
        tvNameBelow: TextView,
        ivQr: ImageView
    ) {
        tvName.text = item.contactName
        tvPhone.text = item.phoneNumber
        tvDate.text = item.createdAt
        tvNameBelow.text = item.contactName

        qrBitmap = QRCodeUtils.generateQRCodeBitmap(item.qrData, 600, 600)
        if (qrBitmap != null) {
            ivQr.setImageBitmap(qrBitmap)
        }
    }

    private fun confirmDelete() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_item_confirm_title)
            .setMessage(R.string.delete_item_confirm_msg)
            .setPositiveButton(R.string.delete) { _, _ ->
                historyItem?.let { dbHelper.deleteQRHistory(it.id) }
                Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showSuccessDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_qr_saved, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<MaterialButton>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
