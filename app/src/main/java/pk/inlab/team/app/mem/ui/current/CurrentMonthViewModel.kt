package pk.inlab.team.app.mem.ui.current

import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.repository.PurchaseRepository

class CurrentMonthViewModel(private val repository: PurchaseRepository) : ViewModel() {

    fun getAllItemsRealTime() = repository.getAllItemsRealtime()

    fun addNewItem(purchaseItem: PurchaseItem) = repository.addNewItem(purchaseItem)
}