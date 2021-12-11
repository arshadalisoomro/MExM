package pk.inlab.team.app.mem.ui.current

import androidx.lifecycle.ViewModel
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.repository.PurchaseItemRepository

class CurrentMonthViewModel(private val itemRepository: PurchaseItemRepository) : ViewModel() {

    fun getAllItemsRealTime() = itemRepository.getAllItemsRealtime()

    fun addNewItem(purchaseItem: PurchaseItem) = itemRepository.addNewItem(purchaseItem)

    fun deleteSelectedItem(documentId: String) = itemRepository.deleteSelectedItem(documentId)
}