package pk.inlab.team.app.mem.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.utils.Constants
import pk.inlab.team.app.mem.utils.State

class PurchaseRepository {

    private val mPurchaseItemsCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PURCHASE)

    /**
     * Returns Flow of [State] which retrieves all PurchaseItems from cloud firestore collection.
     */
    fun getAllItemsRealtime() : Flow<State<List<PurchaseItem>>> = callbackFlow {

        // Register listener
        val listener = mPurchaseItemsCollection.addSnapshotListener { snapshot, exception ->

            if (snapshot != null) {
                trySend(State.success(snapshot.toObjects(PurchaseItem::class.java)))
            }

            // If exception occurs, cancel this scope with exception message.
            exception?.let {
                trySend(State.failed(it.message.toString())).isSuccess
                cancel(it.message.toString())
            }
        }

        awaitClose {
            // This block is executed when producer channel is cancelled
            // This function resumes with a cancellation exception.

            // Dispose listener
            listener.remove()
            cancel()
        }
    }

    /**
     * Adds purchaseItem [purchaseItem] into the cloud firestore collection.
     * @return The Flow of [State] which will store state of current action.
     */
    fun addNewItem(purchaseItem: PurchaseItem) = flow<State<DocumentReference>> {

        // Emit loading state
        emit(State.loading())

        val postRef = mPurchaseItemsCollection.add(purchaseItem).await()

        // Emit success state with post reference
        emit(State.success(postRef))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}