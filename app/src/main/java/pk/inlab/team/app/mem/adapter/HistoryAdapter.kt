package pk.inlab.team.app.mem.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import pk.inlab.team.app.mem.databinding.HistoryMonthHeaderBinding
import pk.inlab.team.app.mem.databinding.ItemCurrentBinding
import pk.inlab.team.app.mem.model.DataItem

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HistoryAdapter:
        ListAdapter<DataItem, RecyclerView.ViewHolder>(HistoryDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> {
                val binding = HistoryMonthHeaderBinding.inflate(LayoutInflater.from(parent.context))
                HeaderViewHolder(binding)
            }
            ITEM_VIEW_TYPE_ITEM -> {
                val binding = ItemCurrentBinding.inflate(LayoutInflater.from(parent.context))
                HistoryItemViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HistoryItemViewHolder -> {
                val historyItem = getItem(position) as DataItem.PurchaseItem
                holder.textView.text = historyItem.purchase.purchaseWeight
            }
            is HeaderViewHolder -> {
                val header = getItem(position) as DataItem.Header
                holder.textView.text = header.title
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.PurchaseItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    inner class HistoryItemViewHolder(binding: ItemCurrentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView: MaterialTextView = binding.tvItemValue
    }

    inner class HeaderViewHolder(binding: HistoryMonthHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView: MaterialTextView = binding.text
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

}