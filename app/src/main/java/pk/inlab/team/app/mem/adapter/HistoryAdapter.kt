package pk.inlab.team.app.mem.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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


        // Long Click listener to show Edit Delete Popup menu
        holder.itemContainer.setOnLongClickListener {
            showEditDeletePopUpMenu(rootView, it, R.menu.menu_pop_up_history)
            return@setOnLongClickListener true
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

    @SuppressLint("RestrictedApi")
    private fun showEditDeletePopUpMenu(rootView: View, v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.option_edit -> {
                    Snackbar.make(rootView, "Edit Clicked", Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnMenuItemClickListener true
                }
                R.id.option_delete -> {
                    Snackbar.make(rootView, "Delete Clicked", Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }

        }

        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {

                val ICON_MARGIN = 16

                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, ICON_MARGIN.toFloat(), rootView.resources.displayMetrics)
                        .toInt()
                if (item.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx,0)
                    } else {
                        item.icon =
                            object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                    }
                }
            }
        }

        // Show the popup menu.
        popup.show()
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