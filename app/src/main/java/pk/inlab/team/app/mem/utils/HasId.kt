package pk.inlab.team.app.mem.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import pk.inlab.team.app.mem.model.PurchaseItem

interface HasId {
    var id : String
}

inline fun <reified T : HasId> DocumentSnapshot.toObjectWithId(): T {
    return this.toObject(T::class.java)!!.also {
        it.id = this.id
    }
}

inline fun <reified T : HasId> QuerySnapshot.toObjectsWithId(java: Class<PurchaseItem>): List<T> {
    return this.documents.map {
        it.toObjectWithId<T>()
    }
}