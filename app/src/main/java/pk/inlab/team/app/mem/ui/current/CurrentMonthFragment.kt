package pk.inlab.team.app.mem.ui.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pk.inlab.team.app.mem.R
import pk.inlab.team.app.mem.adapter.CurrentMonthAdapter
import pk.inlab.team.app.mem.databinding.CurrentItemInfoBinding
import pk.inlab.team.app.mem.databinding.FragmentCurrentBinding
import pk.inlab.team.app.mem.databinding.InputPurchaseItemBinding
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.ui.views.GridDividerItemDecoration
import pk.inlab.team.app.mem.utils.DateUtils
import pk.inlab.team.app.mem.utils.State
import pk.inlab.team.app.mem.utils.Utils.Companion.showIconsOnPopupMenu
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CurrentMonthFragment : Fragment(),
    CurrentMonthAdapter.OnItemClickListener,
    CurrentMonthAdapter.OnItemLongClickListener
{

    private lateinit var currentMonthViewModel: CurrentMonthViewModel
    private var _binding: FragmentCurrentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Coroutine Scope
    private val uiScope = CoroutineScope(Dispatchers.Main)

    // RV globally
    private lateinit var recyclerView: RecyclerView

    // RootView a Globally
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        binding.root.also { rootView = it }

        currentMonthViewModel = ViewModelProvider(this, CurrentViewModelFactory())
            .get(CurrentMonthViewModel::class.java)

        recyclerView = binding.recyclerviewCurrent

        // Fetch Data from FireStore using Coroutine
        val adapter = CurrentMonthAdapter(this, this)
        recyclerView.adapter = adapter

        // Launch coroutine
        uiScope.launch {
            loadItems(adapter)
        }
        return rootView

    }

    private fun setGridItemDecorator(recyclerView: RecyclerView) {
        // Set Grid Item Divider
        val horizontalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.item_divider)
        val verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.item_divider)

        recyclerView.addItemDecoration(
            GridDividerItemDecoration(
                horizontalDivider,
                verticalDivider,
                4
            )
        )
    }

    private suspend fun loadItems(adapter: CurrentMonthAdapter) {
        currentMonthViewModel.getAllItemsOfCurrentMonth().collect {


            when(it){
                is State.Loading -> {
                    if (!binding.lpiLoading.isShown) binding.lpiLoading.show()
                }
                is State.Success -> {
                    // Submit data to adapter
                    adapter.submitList(it.data)

                    // List is not empty or less than or Equal to 3 items then set setGridItemDecorator
                    if(it.data.isNotEmpty() && it.data.size >= 3) setGridItemDecorator(recyclerView)

                    if (binding.lpiLoading.isShown) binding.lpiLoading.hide()
                }
                is State.Failed -> {
                    Snackbar.make(rootView, "Error is: ${it.message}", Snackbar.LENGTH_SHORT)
                        .show()
                    if (binding.lpiLoading.isShown) binding.lpiLoading.hide()
                }
            }
        }

    }

    // Show Info Dialog
    override fun onItemClick(purchaseItem: PurchaseItem) {
        showItemInfoDialog(purchaseItem)
    }

    private fun showItemInfoDialog(item: PurchaseItem) {
        // Through bindings
        val binding = CurrentItemInfoBinding.inflate(LayoutInflater.from(context))

        // Time format
        val time = DateUtils.convertLongToTime(item.purchaseTimeMilli)

        // Get View from binding
        val infoDialogView = binding.root

        // Set Current Item Weight
        binding.mtvCurrentItemInfoWeight.text = requireContext().getString(R.string.current_item_total_paos, item.purchaseWeight)

        // Set Current Item Date
        binding.mtvCurrentItemInfoTime.text = requireContext().getString(R.string.current_item_purchase_time, time)

        // Set Current Item Description
        binding.mtvCurrentItemInfoDescription.text = item.purchaseDescription

        // Date format
        val dateMonthYear = DateUtils.convertLongToDateMonthYear(item.purchaseTimeMilli)

        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle(dateMonthYear)
            .setView(infoDialogView)
            .setPositiveButton(requireContext().resources.getString(R.string.ok))
            { dialog , /*which*/ _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onItemLongClick(currentItemView: View, purchaseItem: PurchaseItem) {
        showEditDeletePopUpMenu(currentItemView, purchaseItem)
    }

    @SuppressLint("RestrictedApi")
    private fun showEditDeletePopUpMenu(v: View, purchaseItem: PurchaseItem) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(R.menu.menu_pop_up, popup.menu)

        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.option_edit -> {
                    showEditDialog(purchaseItem)
                    return@setOnMenuItemClickListener true
                }
                R.id.option_delete -> {
                    deleteItem(purchaseItem.id)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }

        }
        // Set Icons on PopupMenu
        showIconsOnPopupMenu(popup, rootView)

        // Show the popup menu.
        popup.show()
    }

    private fun showEditDialog(purchaseItem: PurchaseItem) {
        // Through bindings
        val binding = InputPurchaseItemBinding.inflate(LayoutInflater.from(requireContext()))

        // Get View from binding
        val inputAlertDialogView = binding.root

        // Set values to EditTexts
        binding.tilDialogItemWeightInPaos.setText(purchaseItem.purchaseWeight.toString())
        binding.tilDialogItemDescription.setText(purchaseItem.purchaseDescription)

        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle(resources.getString(R.string.new_item))
            .setView(inputAlertDialogView)
            .setPositiveButton(resources.getString(R.string.save))
            { /*dialog*/ _ , /*which*/ _ ->
                updateCurrentItem(purchaseItem, binding)

            }
            .setNegativeButton(resources.getString(R.string.cancel))
            {  /*dialog*/ _ , /*which*/ _ ->
                // Just Placeholder
            }
            .show()
    }

    private fun updateCurrentItem(
        purchaseItem: PurchaseItem,
        bindingIPI: InputPurchaseItemBinding
    ) {

        // Launch coroutine
        uiScope.launch {
            currentMonthViewModel.updateCurrentItem(
                PurchaseItem(
                    purchaseItem.id,
                    purchaseItem.purchaseTimeMilli,
                    bindingIPI.tilDialogItemWeightInPaos.text.toString().toInt(),
                    bindingIPI.tilDialogItemDescription.text.toString(),
                )
            ).collect {
                when (it) {
                    is State.Loading -> {
                        if (!binding.lpiLoading.isShown) binding.lpiLoading.show()
                    }
                    is State.Success -> {
                        Snackbar.make(rootView, "Item Updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        if (binding.lpiLoading.isShown) binding.lpiLoading.hide()
                    }
                    is State.Failed -> {
                        if (binding.lpiLoading.isShown) binding.lpiLoading.hide()
                    }
                }
            }
        }
    }

    private fun deleteItem(itemId: String) {

        // Launch coroutine
        uiScope.launch {
            currentMonthViewModel.deleteSelectedItem(itemId).collect {
                when (it) {
                    is State.Loading -> {
                        if (!binding.lpiLoading.isShown) binding.lpiLoading.show()
                    }

                    is State.Success -> {
                        Snackbar.make(rootView, "Item Deleted", Snackbar.LENGTH_SHORT)
                            .show()

                        if (binding.lpiLoading.isShown) binding.lpiLoading.hide()
                    }
                    is State.Failed -> {
                        Snackbar.make(rootView, "Error is: ${it.message}", Snackbar.LENGTH_SHORT)
                            .show()
                        if (binding.lpiLoading.isShown) binding.lpiLoading.hide()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}