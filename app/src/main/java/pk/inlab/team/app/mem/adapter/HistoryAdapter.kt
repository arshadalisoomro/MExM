package pk.inlab.team.app.mem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import pk.inlab.team.app.mem.R
import pk.inlab.team.app.mem.databinding.HistoryItemInfoBinding
import pk.inlab.team.app.mem.databinding.ItemHistoryBinding
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.utils.Utils

class HistoryAdapter(private val rootView: View):
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
        val context = rootView.context

        val item = getItem(position)
        holder.totalMonthExpense.text = (item.purchaseWeight*120).toString()
        holder.totalExpenseMonthAndYear.text = "2022"
        holder.totalMonthPaos.text = "200"
        holder.totalMonthKilos.text = "50"

        // Click listener to show Info
        holder.itemContainer.setOnClickListener {
            showItemInfoDialog(context, item)
        }
    }

    private fun showItemInfoDialog(context: Context, item: PurchaseItem) {
        // Through bindings
        val binding = HistoryItemInfoBinding.inflate(LayoutInflater.from(context))

        // Time format
        val time = Utils.convertLongToTime(item.purchaseTimeMilli)

        // Get View from binding
        val infoDialogView = binding.root

        // Set Current Item Weight
        binding.mtvHistoryItemInfoWeight.text = item.purchaseWeight.toString()

        // Set Current Item Date
        binding.mtvHistoryItemInfoMonth.text = time

        // Set Current Item Description
        binding.mtvHistoryItemInfoDescription.text = item.purchaseDescription

        // Date format
        val dateMonthYear = Utils.convertLongToDateMonthYear(item.purchaseTimeMilli)

        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setTitle(dateMonthYear)
            .setView(infoDialogView)
            .setPositiveButton(context.resources.getString(R.string.ok))
            { dialog , /*which*/ _ ->
                dialog.dismiss()
            }
            .show()
    }

    inner class History(binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val itemContainer: ConstraintLayout = binding.clItemHistoryContainer

        val totalMonthExpense: MaterialTextView = binding.mtvMilkTotalMonthExpense
        val totalExpenseMonthAndYear: MaterialTextView = binding.mtvMilkExpenseMonthYear
        val totalMonthPaos: MaterialTextView = binding.mtvMilkMonthTotalPaos
        val totalMonthKilos: MaterialTextView = binding.mtvMilkMonthTotalKilos
    }

}