package pk.inlab.team.app.mem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import pk.inlab.team.app.mem.R
import pk.inlab.team.app.mem.databinding.CurrentItemInfoBinding
import pk.inlab.team.app.mem.databinding.ItemCurrentBinding
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.utils.DateUtils.Companion.convertLongToDateMonthYear
import pk.inlab.team.app.mem.utils.DateUtils.Companion.convertLongToDayDate
import pk.inlab.team.app.mem.utils.DateUtils.Companion.convertLongToTime

class CurrentMonthAdapter(
    private val rootView: View,
    private val listener: OnItemLongClickListener
) : ListAdapter<PurchaseItem, CurrentMonthAdapter.CurrentMonth>(
    object : DiffUtil.ItemCallback<PurchaseItem>() {

            override fun areItemsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean =
                oldItem == newItem
        }
) {


    /**
     * Handles Long click on item for Edit and delete operation
     */
    interface OnItemLongClickListener {
        fun onItemLongClick(currentItemView: View, itemId: String)
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentMonth {
            val binding = ItemCurrentBinding.inflate(LayoutInflater.from(parent.context))
            return CurrentMonth(binding)
        }

        override fun onBindViewHolder(holder: CurrentMonth, position: Int) {
            val context = rootView.context
            val item = getItem(position)
            holder.itemWeightInPaos.text = item.purchaseWeight.toString()
            holder.itemPurchaseDate.text = convertLongToDayDate(item.purchaseTimeMilli)

            // Click listener to show Info
            holder.itemContainer.setOnClickListener {
                showItemInfoDialog(context, item)
            }

            // Long Click listener to show Edit Delete Popup menu
            holder.itemContainer.setOnLongClickListener {
                // showEditDeletePopUpMenu(rootView, it, R.menu.menu_pop_up)
                // Pass the id of Current Item for further process
                listener.onItemLongClick(it, item.id)
                return@setOnLongClickListener true
            }

        }

    private fun showItemInfoDialog(context: Context, item: PurchaseItem) {
        // Through bindings
        val binding = CurrentItemInfoBinding.inflate(LayoutInflater.from(context))

        // Time format
        val time = convertLongToTime(item.purchaseTimeMilli)

        // Get View from binding
        val infoDialogView = binding.root

        // Set Current Item Weight
        binding.mtvCurrentItemInfoWeight.text = item.purchaseWeight.toString()

        // Set Current Item Date
        binding.mtvCurrentItemInfoTime.text = time

        // Set Current Item Description
        binding.mtvCurrentItemInfoDescription.text = item.purchaseDescription

        // Date format
        val dateMonthYear = convertLongToDateMonthYear(item.purchaseTimeMilli)

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

    inner class CurrentMonth(binding: ItemCurrentBinding) :
            RecyclerView.ViewHolder(binding.root) {

            val itemContainer: LinearLayoutCompat = binding.llcItemCurrentContainer

            val itemWeightInPaos: MaterialTextView = binding.mtvItemPaos
            val itemPurchaseDate: MaterialTextView = binding.mtvItemPurchaseDate
        }
    }