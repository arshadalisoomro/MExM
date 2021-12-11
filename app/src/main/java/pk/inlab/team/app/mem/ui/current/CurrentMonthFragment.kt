package pk.inlab.team.app.mem.ui.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.ui.views.GridDividerItemDecoration
import pk.inlab.team.app.mem.utils.DateUtils
import pk.inlab.team.app.mem.utils.State
import pk.inlab.team.app.mem.utils.Utils.Companion.showIconsOnPopupMenu


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
        currentMonthViewModel.getAllItemsRealTime().collect {
            when(it){
                is State.Loading -> {
                    showToast(rootView, "Loading")
                }
                is State.Success -> {
                    Log.e("__DATA__", it.data.toString())

                    adapter.submitList(it.data)

                    // List is not empty or less than or Equal to 3 items then set setGridItemDecorator
                    if(it.data.isNotEmpty() && it.data.size >= 3) setGridItemDecorator(recyclerView)


                }
                is State.Failed -> {
                    Log.e("__DATA__", it.message)
                    showToast(rootView, "Failed!")
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
        binding.mtvCurrentItemInfoWeight.text = item.purchaseWeight.toString()

        // Set Current Item Date
        binding.mtvCurrentItemInfoTime.text = time

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

    override fun onItemLongClick(currentItemView: View, itemId: String) {
        showEditDeletePopUpMenu(currentItemView, itemId)
    }

    @SuppressLint("RestrictedApi")
    private fun showEditDeletePopUpMenu(v: View, itemId: String) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(R.menu.menu_pop_up, popup.menu)

        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.option_edit -> {
                    Snackbar.make(rootView, "Edit Clicked", Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnMenuItemClickListener true
                }
                R.id.option_delete -> {
                    // Launch coroutine
                    deleteItem(itemId)
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

    private fun deleteItem(itemId: String) {
        uiScope.launch {
            currentMonthViewModel.deleteSelectedItem(itemId).collect {
                when (it) {
                    is State.Loading -> {
                        showToast(rootView, "Loading")
                    }

                    is State.Success -> {
                        Snackbar.make(rootView, "Item Deleted", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is State.Failed -> {
                        Log.e("__DATA__", it.message)
                        showToast(rootView, "Failed!")
                    }
                }
            }
        }
    }

    private fun showToast(root: View, message: String) {
        Toast.makeText(root.context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}