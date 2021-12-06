package pk.inlab.team.app.mem.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.DataItem
import pk.inlab.team.app.mem.model.Purchase
import java.util.*

class HistoryViewModel : ViewModel() {

    private val list = listOf(
        Purchase("45rfdgh0", Date().time, "12", "Description"),
        Purchase("45rfdgh1", Date().time, "12", "Description"),
        Purchase("45rfdgh2", Date().time, "12", "Description"),
        Purchase("45rfdgh3", Date().time, "12", "Description"),
        Purchase("45rfdgh4", Date().time, "12", "Description"),
        Purchase("45rfdgh5", Date().time, "12", "Description"),
        Purchase("45rfdgh6", Date().time, "12", "Description"),
        Purchase("45rfdgh7", Date().time, "12", "Description"),
        Purchase("45rfdgh8", Date().time, "12", "Description"),
    )

    private val _dataItems = MutableLiveData<List<DataItem>>().apply {
        value = when (list) {
            null -> listOf(DataItem.Header("Title"))
            else -> listOf(DataItem.Header("Title")) + list.map { DataItem.PurchaseItem(it) }
        }
    }

    val dataItems: LiveData<List<DataItem>> = _dataItems
}