package pk.inlab.team.app.mem.model

data class Purchase(
        var purchaseId: String,

        val purchaseTimeMilli: Long = System.currentTimeMillis(),

        var purchaseWeight: String,
        var purchaseDescription: String,
)