package pk.inlab.team.app.mem.ui.current

import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.repository.CurrentMonthPurchaseRepository

class CurrentMonthViewModel(private val itemRepository: CurrentMonthPurchaseRepository) : ViewModel() {

    fun getAllItemsOfCurrentMonth() = itemRepository.getAllItemsOfCurrentMonth()

    fun addNewItemToCurrentMonth(purchaseItem: PurchaseItem) = itemRepository.addNewItemToCurrentMonth(purchaseItem)

    fun updateCurrentItemOfCurrentMonth(purchaseItem: PurchaseItem) = itemRepository.updateCurrentItemOfCurrentMonth(purchaseItem)

    fun deleteCurrentItemOfCurrentMonth(documentId: String) = itemRepository.deleteCurrentItemOfCurrentMonth(documentId)
}