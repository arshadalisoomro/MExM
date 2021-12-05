package pk.inlab.team.app.mem.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import pk.inlab.team.app.mem.adapter.CurrentMonthAdapter
import pk.inlab.team.app.mem.databinding.FragmentCurrentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CurrentMonthFragment : Fragment() {

    private val currentMonthViewModel: CurrentMonthViewModel by viewModels()
    private var _binding: FragmentCurrentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerviewCurrent

        val adapter = CurrentMonthAdapter()
        recyclerView.adapter = adapter
        currentMonthViewModel.texts.observe(viewLifecycleOwner, {
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