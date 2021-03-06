package pk.inlab.team.app.mem.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import pk.inlab.team.app.mem.R
import pk.inlab.team.app.mem.adapter.HistoryAdapter
import pk.inlab.team.app.mem.databinding.FragmentHistoryBinding
import pk.inlab.team.app.mem.ui.views.GridDividerItemDecoration

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerviewHistory
        // Set Grid Item Divider
        val horizontalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.item_divider)
        val verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.item_divider)

        recyclerView.addItemDecoration(
            GridDividerItemDecoration(
                horizontalDivider,
                verticalDivider,
                2
            )
        )
        val adapter = HistoryAdapter(root)
        recyclerView.adapter = adapter
        // Set values to RV
        historyViewModel.purchaseItems.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}