package pk.inlab.team.app.mem.model

import pk.inlab.team.app.mem.utils.HasId

data class PurchaseItem(
        override var id: String = "",
        val purchaseTimeMilli: Long = System.currentTimeMillis(),
        var purchaseWeight: Int = 0,
        var purchaseDescription: String? = null,
): HasId