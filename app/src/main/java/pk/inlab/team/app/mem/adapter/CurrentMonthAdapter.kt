package pk.inlab.team.app.mem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import pk.inlab.team.app.mem.databinding.ItemCurrentBinding
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.utils.DateUtils.Companion.convertLongToDate

class CurrentMonthAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
) : ListAdapter<PurchaseItem, CurrentMonthAdapter.CurrentMonth>(
    object : DiffUtil.ItemCallback<PurchaseItem>() {

            override fun areItemsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean =
                oldItem == newItem
        }
) {


    /**
     * Handles Single click on item for Info Dialog
     */
    interface OnItemClickListener {
        /**
         * Shows Info of selected purchaseItem  [purchaseItem] from the list fetched from
         * the cloud firestore collection.
         */
        fun onItemClick(purchaseItem: PurchaseItem)
    }

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
            val item = getItem(position)
            holder.itemWeightInPaos.text = item.purchaseWeight.toString()
            holder.itemPurchaseDate.text = convertLongToDate(item.purchaseTimeMilli)

            // Click listener to show Info
            holder.itemContainer.setOnClickListener {
                // showItemInfoDialog(context, item)
                // Pass the Current Item for further process
                onItemClickListener.onItemClick(item)
            }

            // Long Click listener to show Edit Delete Popup menu
            holder.itemContainer.setOnLongClickListener {
                // showEditDeletePopUpMenu(rootView, it, R.menu.menu_pop_up)
                // Pass the id of Current Item for further process
                onItemLongClickListener.onItemLongClick(it, item.id)
                return@setOnLongClickListener true
            }

        }

    inner class CurrentMonth(binding: ItemCurrentBinding) :
            RecyclerView.ViewHolder(binding.root) {

            val itemContainer: LinearLayoutCompat = binding.llcItemCurrentContainer

            val itemWeightInPaos: MaterialTextView = binding.mtvItemPaos
            val itemPurchaseDate: MaterialTextView = binding.mtvItemPurchaseDate
        }
    }