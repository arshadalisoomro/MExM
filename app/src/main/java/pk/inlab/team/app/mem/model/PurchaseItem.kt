package pk.inlab.team.app.mem.model

import com.google.firebase.firestore.Exclude
import pk.inlab.team.app.mem.utils.HasId

data class PurchaseItem(
        @get:Exclude
        override var id: String = "",
        val purchaseTimeMilli: Long = System.currentTimeMillis(),
        var purchaseWeight: Int = 0,
        var purchaseDescription: String? = null,
): HasId