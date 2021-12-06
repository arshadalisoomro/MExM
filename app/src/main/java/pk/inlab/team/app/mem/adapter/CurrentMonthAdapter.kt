package pk.inlab.team.app.mem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import pk.inlab.team.app.mem.databinding.ItemCurrentBinding

class CurrentMonthAdapter :
        ListAdapter<String, CurrentMonthAdapter.CurrentMonth>(object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentMonth {
            val binding = ItemCurrentBinding.inflate(LayoutInflater.from(parent.context))
            return CurrentMonth(binding)
        }

        override fun onBindViewHolder(holder: CurrentMonth, position: Int) {
            holder.textView.text = getItem(position)
        }

        inner class CurrentMonth(binding: ItemCurrentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val textView: MaterialTextView = binding.tvItemValue
        }
    }