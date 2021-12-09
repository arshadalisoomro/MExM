package pk.inlab.team.app.mem.model

data class PurchaseItem(
        var purchaseId: String? = null,
        val purchaseTimeMilli: Long = System.currentTimeMillis(),
        var purchaseWeight: Int = 0,
        var purchaseDescription: String? = null,
)