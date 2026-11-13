package com.project.qrvault.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.qrvault.MainActivity
import com.project.qrvault.QRViewerActivity
import com.project.qrvault.R
import com.project.qrvault.adapter.QRHistoryAdapter
import com.project.qrvault.database.DatabaseHelper
import com.project.qrvault.model.QRHistoryModel
import com.project.qrvault.utils.ImageUtils
import com.project.qrvault.utils.QRCodeUtils

class QRHistoryFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvHistory: RecyclerView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var btnClearAll: ImageButton
    private lateinit var btnEmptyGenerate: MaterialButton

    private var adapter: QRHistoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qr_history, container, false)
        dbHelper = DatabaseHelper(requireContext())

        rvHistory = view.findViewById(R.id.rvQrHistory)
        layoutEmpty = view.findViewById(R.id.layoutEmptyHistory)
        btnClearAll = view.findViewById(R.id.btnClearAllHistory)
        btnEmptyGenerate = view.findViewById(R.id.btnEmptyGenerate)

        rvHistory.layoutManager = LinearLayoutManager(requireContext())

        btnClearAll.setOnClickListener {
            confirmClearAllHistory()
        }

        btnEmptyGenerate.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.navigation_contacts)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadHistoryData()
    }

    private fun loadHistoryData() {
        val historyList = dbHelper.getAllQRHistory()

        if (historyList.isEmpty()) {
            rvHistory.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
        } else {
            rvHistory.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE

            adapter = QRHistoryAdapter(
                historyList = historyList,
                onItemClick = { item ->
                    openViewer(item)
                },
                onItemShare = { item ->
                    shareItem(item)
                },
                onItemDelete = { item ->
                    confirmDeleteItem(item)
                }
            )
            rvHistory.adapter = adapter
        }
    }

    private fun openViewer(item: QRHistoryModel) {
        val intent = Intent(requireContext(), QRViewerActivity::class.java).apply {
            putExtra("QR_HISTORY_ITEM", item)
        }
        startActivity(intent)
    }

    private fun shareItem(item: QRHistoryModel) {
        val bitmap = QRCodeUtils.generateQRCodeBitmap(item.qrData, 512, 512)
        if (bitmap != null) {
            ImageUtils.shareBitmap(requireContext(), bitmap, item.contactName)
        }
    }

    private fun confirmDeleteItem(item: QRHistoryModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_item_confirm_title)
            .setMessage(R.string.delete_item_confirm_msg)
            .setPositiveButton(R.string.delete) { _, _ ->
                dbHelper.deleteQRHistory(item.id)
                loadHistoryData()
                Toast.makeText(requireContext(), "Record deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun confirmClearAllHistory() {
        val historyList = dbHelper.getAllQRHistory()
        if (historyList.isEmpty()) {
            Toast.makeText(requireContext(), "History is already empty", Toast.LENGTH_SHORT).show()
            return
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.clear_history_confirm_title)
            .setMessage(R.string.clear_history_confirm_msg)
            .setPositiveButton(R.string.clear) { _, _ ->
                dbHelper.clearAllHistory()
                loadHistoryData()
                Toast.makeText(requireContext(), "All history cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
