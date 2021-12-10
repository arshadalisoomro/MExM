package pk.inlab.team.app.mem.ui.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pk.inlab.team.app.mem.R
import pk.inlab.team.app.mem.adapter.CurrentMonthAdapter
import pk.inlab.team.app.mem.databinding.FragmentCurrentBinding
import pk.inlab.team.app.mem.ui.views.GridDividerItemDecoration
import pk.inlab.team.app.mem.utils.State


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CurrentMonthFragment : Fragment() {

    private lateinit var currentMonthViewModel: CurrentMonthViewModel
    private var _binding: FragmentCurrentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Coroutine Scope
    private val uiScope = CoroutineScope(Dispatchers.Main)

    // RV globally
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        currentMonthViewModel = ViewModelProvider(this, CurrentViewModelFactory())
            .get(CurrentMonthViewModel::class.java)

        recyclerView = binding.recyclerviewCurrent

        // Fetch Data from FireStore using Coroutine
        val adapter = CurrentMonthAdapter(root)
        recyclerView.adapter = adapter

        // Launch coroutine
        uiScope.launch {
            loadItems(root, adapter)
        }
        return root

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

    private suspend fun loadItems(root: View, adapter: CurrentMonthAdapter) {
        currentMonthViewModel.getAllItemsRealTime().collect {
            when(it){
                is State.Loading -> {
                    showToast(root, "Loading")
                }
                is State.Success -> {
                    Log.e("__DATA__", it.data.toString())

                    adapter.submitList(it.data)
                    // In favor of avoiding NullPointer Exception ;-)
                    try {
                        // List is not empty or less than or Equal to 3 items then set setGridItemDecorator
                        if(it.data.isNotEmpty() && it.data.size >= 3) setGridItemDecorator(recyclerView)
                    } catch (e: Exception) {

                    }

                }
                is State.Failed -> {
                    Log.e("__DATA__", it.message)
                    showToast(root, "Failed!")
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