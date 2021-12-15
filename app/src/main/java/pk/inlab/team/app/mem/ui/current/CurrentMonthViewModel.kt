package pk.inlab.team.app.mem.ui.current

import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.repository.PurchaseItemRepository

class CurrentMonthViewModel(private val itemRepository: PurchaseItemRepository) : ViewModel() {

    fun getAllItemsOfCurrentMonth() = itemRepository.getAllItemsOfCurrentMonth()

    fun getAllItemsRealTime() = itemRepository.getAllItemsRealtime()

    fun addNewItemToCurrentMonth(purchaseItem: PurchaseItem) = itemRepository.addNewItemToCurrentMonth(purchaseItem)

    fun addNewItem(purchaseItem: PurchaseItem) = itemRepository.addNewItem(purchaseItem)

    fun updateCurrentItem(purchaseItem: PurchaseItem) = itemRepository.updateCurrentItem(purchaseItem)

    fun deleteSelectedItem(documentId: String) = itemRepository.deleteSelectedItem(documentId)
}