package com.project.qrvault.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.project.qrvault.R
import com.project.qrvault.model.QRHistoryModel
import com.project.qrvault.utils.QRCodeUtils

class QRHistoryAdapter(
    private var historyList: List<QRHistoryModel>,
    private val onItemClick: (QRHistoryModel) -> Unit,
    private val onItemShare: (QRHistoryModel) -> Unit,
    private val onItemDelete: (QRHistoryModel) -> Unit
) : RecyclerView.Adapter<QRHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_qr_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.tvHistoryName.text = item.contactName
        holder.tvHistoryPhone.text = item.phoneNumber
        holder.tvHistoryDate.text = item.createdAt

        // Render mini QR code thumbnail
        val qrBitmap = QRCodeUtils.generateQRCodeBitmap(item.qrData, 120, 120)
        if (qrBitmap != null) {
            holder.ivQrThumbnail.setImageBitmap(qrBitmap)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        holder.btnOverflow.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.menu.add(0, 1, 0, "View QR Code")
            popup.menu.add(0, 2, 1, "Share QR Code")
            popup.menu.add(0, 3, 2, "Delete Record")

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    1 -> onItemClick(item)
                    2 -> onItemShare(item)
                    3 -> onItemDelete(item)
                }
                true
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun updateList(newList: List<QRHistoryModel>) {
        historyList = newList
        notifyDataSetChanged()
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivQrThumbnail: ImageView = itemView.findViewById(R.id.ivQrThumbnail)
        val tvHistoryName: TextView = itemView.findViewById(R.id.tvHistoryName)
        val tvHistoryPhone: TextView = itemView.findViewById(R.id.tvHistoryPhone)
        val tvHistoryDate: TextView = itemView.findViewById(R.id.tvHistoryDate)
        val btnOverflow: ImageButton = itemView.findViewById(R.id.btnOverflow)
    }
}
