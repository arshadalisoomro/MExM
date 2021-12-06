package pk.inlab.team.app.mem.model

sealed class DataItem {

 abstract val id: String

 data class PurchaseItem(val purchase: Purchase): DataItem(){
  override val id = purchase.purchaseId

 }

 data class Header(val title: String): DataItem(){
  override val id = Long.MIN_VALUE.toString()
 }


}