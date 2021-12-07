package pk.inlab.team.app.mem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import pk.inlab.team.app.mem.databinding.ItemHistoryBinding
import pk.inlab.team.app.mem.model.PurchaseItem

class HistoryAdapter:
    ListAdapter<PurchaseItem, HistoryAdapter.History>(object : DiffUtil.ItemCallback<PurchaseItem>() {

        override fun areItemsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean =
            oldItem == newItem
    }){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): History {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context))
        return History(binding)
    }

    override fun onBindViewHolder(holder: History, position: Int) {
        val item = getItem(position)
        holder.totalMonthExpense.text = (item.purchaseWeight*120).toString()
        holder.totalExpenseMonthAndYear.text = "2022"
        holder.totalMonthPaos.text = "200"
        holder.totalMonthKilos.text = "50"
    }

    inner class History(binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val totalMonthExpense: MaterialTextView = binding.mtvMilkTotalMonthExpense
        val totalExpenseMonthAndYear: MaterialTextView = binding.mtvMilkExpenseMonthYear
        val totalMonthPaos: MaterialTextView = binding.mtvMilkMonthTotalPaos
        val totalMonthKilos: MaterialTextView = binding.mtvMilkMonthTotalKilos
    }

}