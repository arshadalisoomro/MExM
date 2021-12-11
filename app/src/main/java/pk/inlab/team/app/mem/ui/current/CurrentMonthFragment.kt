package pk.inlab.team.app.mem.ui.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pk.inlab.team.app.mem.R
import pk.inlab.team.app.mem.adapter.CurrentMonthAdapter
import pk.inlab.team.app.mem.databinding.FragmentCurrentBinding
import pk.inlab.team.app.mem.ui.views.GridDividerItemDecoration
import pk.inlab.team.app.mem.utils.State
import pk.inlab.team.app.mem.utils.Utils.Companion.showIconsOnPopupMenu


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CurrentMonthFragment : Fragment(), CurrentMonthAdapter.OnItemLongClickListener {

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
        val adapter = CurrentMonthAdapter(rootView, this)
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
                3
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


    override fun onItemLongClick(currentItemView: View, itemId: String) {
        showEditDeletePopUpMenu(currentItemView, R.menu.menu_pop_up)
    }

    @SuppressLint("RestrictedApi")
    private fun showEditDeletePopUpMenu(v: View, @MenuRes menuRes: Int) {
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
        // Set Icons on PopupMenu
        showIconsOnPopupMenu(popup, rootView)

        // Show the popup menu.
        popup.show()
    }

    private fun showToast(root: View, message: String) {
        Toast.makeText(root.context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}