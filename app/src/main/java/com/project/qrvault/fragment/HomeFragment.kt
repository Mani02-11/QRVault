package com.project.qrvault.fragment

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.project.qrvault.MainActivity
import com.project.qrvault.QRViewerActivity
import com.project.qrvault.R
import com.project.qrvault.adapter.QRHistoryAdapter
import com.project.qrvault.database.DatabaseHelper
import com.project.qrvault.utils.ImageUtils
import com.project.qrvault.utils.PermissionUtils
import com.project.qrvault.utils.QRCodeUtils

class HomeFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvTotalContactsCount: TextView
    private lateinit var tvQrGeneratedCount: TextView
    private lateinit var rvRecentQrs: RecyclerView
    private lateinit var tvRecentEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        dbHelper = DatabaseHelper(requireContext())

        tvTotalContactsCount = view.findViewById(R.id.tvTotalContactsCount)
        tvQrGeneratedCount = view.findViewById(R.id.tvQrGeneratedCount)
        rvRecentQrs = view.findViewById(R.id.rvRecentQrs)
        tvRecentEmpty = view.findViewById(R.id.tvRecentEmpty)

        val cardGenerate = view.findViewById<MaterialCardView>(R.id.cardQuickGenerate)
        val cardContacts = view.findViewById<MaterialCardView>(R.id.cardQuickContacts)
        val cardHistory = view.findViewById<MaterialCardView>(R.id.cardQuickHistory)
        val tvViewAll = view.findViewById<TextView>(R.id.tvViewAllRecent)

        cardGenerate.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.navigation_contacts)
        }

        cardContacts.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.navigation_contacts)
        }

        cardHistory.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.navigation_history)
        }

        tvViewAll.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.navigation_history)
        }

        rvRecentQrs.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onResume() {
        super.onResume()
        loadDashboardStats()
    }

    private fun loadDashboardStats() {
        // 1. Load QR count from SQLite
        val qrCount = dbHelper.getHistoryCount()
        tvQrGeneratedCount.text = qrCount.toString()

        // 2. Load Contact count if permission granted
        val contactCount = if (PermissionUtils.hasReadContactsPermission(requireContext())) {
            getDeviceContactsCount()
        } else {
            0
        }
        tvTotalContactsCount.text = contactCount.toString()

        // 3. Load recent QRs
        val recentList = dbHelper.getRecentHistory(3)
        if (recentList.isEmpty()) {
            rvRecentQrs.visibility = View.GONE
            tvRecentEmpty.visibility = View.VISIBLE
        } else {
            rvRecentQrs.visibility = View.VISIBLE
            tvRecentEmpty.visibility = View.GONE

            val adapter = QRHistoryAdapter(
                historyList = recentList,
                onItemClick = { item ->
                    val intent = Intent(requireContext(), QRViewerActivity::class.java).apply {
                        putExtra("QR_HISTORY_ITEM", item)
                    }
                    startActivity(intent)
                },
                onItemShare = { item ->
                    val bitmap = QRCodeUtils.generateQRCodeBitmap(item.qrData, 512, 512)
                    if (bitmap != null) {
                        ImageUtils.shareBitmap(requireContext(), bitmap, item.contactName)
                    }
                },
                onItemDelete = { item ->
                    dbHelper.deleteQRHistory(item.id)
                    loadDashboardStats()
                }
            )
            rvRecentQrs.adapter = adapter
        }
    }

    private fun getDeviceContactsCount(): Int {
        var count = 0
        try {
            val cursor: Cursor? = requireContext().contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            count = cursor?.count ?: 0
            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return count
    }
}
