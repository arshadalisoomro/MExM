package pk.inlab.team.app.mem.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.PurchaseItem
import java.util.*

class HistoryViewModel : ViewModel() {

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (1..60).mapIndexed { _, i ->
            "Item # $i"
        }
    }

    val texts: LiveData<List<String>> = _texts


    private val  _purchaseItems = MutableLiveData<List<PurchaseItem>>().apply {
        value = (1..60).mapIndexed { _, i ->
            // "Item # $i"
            PurchaseItem("id$i", Date().time, 23, "Some Desc $i")
        }
    }

    val purchaseItems: LiveData<List<PurchaseItem>> = _purchaseItems
}