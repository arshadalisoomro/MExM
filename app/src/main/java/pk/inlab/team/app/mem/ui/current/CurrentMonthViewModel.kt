package pk.inlab.team.app.mem.ui.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.PurchaseItem
import java.util.*

class CurrentMonthViewModel : ViewModel() {

    private val  _purchaseItems = MutableLiveData<List<PurchaseItem>>().apply {
        value = (1..30).mapIndexed { _, i ->
            // "Item # $i"
            PurchaseItem("id$i", Date().time, (3*i), "Some Desc $i")
        }
    }

    val purchaseItems: LiveData<List<PurchaseItem>> = _purchaseItems
}