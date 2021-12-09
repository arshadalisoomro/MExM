package pk.inlab.team.app.mem.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    fun getAllItems() = flow<State<List<PurchaseItem>>> {

        // Emit loading state
        emit(State.loading())

        val snapshot = mPurchaseItemsCollection.get().await()
        val posts = snapshot.toObjects(PurchaseItem::class.java)

        // Emit success state with data
        emit(State.success(posts))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

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