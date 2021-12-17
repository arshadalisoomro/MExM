package pk.inlab.team.app.mem.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.utils.Constants
import pk.inlab.team.app.mem.utils.DateUtils
import pk.inlab.team.app.mem.utils.State
import pk.inlab.team.app.mem.utils.toObjectsWithId

class CurrentMonthPurchaseRepository {

    // Main Document for Each Year
    private var currentYear = DateUtils.getCurrentYear()

    // Separate Collections for Each Month of Each Year
    private var currentMonthWithStartAndEndDate = DateUtils.getCurrentMonthWithStartEndDate()

    private val mCurrentMonthPurchaseItemsCollection = FirebaseFirestore.getInstance()
                                                            .collection(Constants.COLLECTION_PURCHASE_ITEM)
                                                            .document(currentYear)
                                                            .collection(currentMonthWithStartAndEndDate)
    /**
     * Returns ProducerScope of [State] which retrieves all PurchaseItems from cloud firestore collection.
     */
    fun getAllItemsOfCurrentMonth() : Flow<State<List<PurchaseItem>>> = callbackFlow {

        // Register listener
        val listener = mCurrentMonthPurchaseItemsCollection
            .orderBy("purchaseTimeMilli", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                if (snapshot != null) {
                    trySend(
                        State.success(snapshot.toObjectsWithId(PurchaseItem::class.java))
                    )
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
    fun addNewItemToCurrentMonth(purchaseItem: PurchaseItem) = flow<State<DocumentReference>> {

        // Emit loading state
        emit(State.loading())

        val postRef = mCurrentMonthPurchaseItemsCollection.add(purchaseItem).await()

        // Emit success state with post reference
        emit(State.success(postRef))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Updates pre-existing purchaseItem [purchaseItem] into the cloud firestore collection.
     * @return The Flow of [State] which will store state of current action.
     */
    fun updateCurrentItemOfCurrentMonth(purchaseItem: PurchaseItem) = flow<State<DocumentReference>> {

        var  failureMessage = ""
        var isUpdationFailed = false


        // Emit loading state
        emit(State.loading())

        val postRef = mCurrentMonthPurchaseItemsCollection
            .document(purchaseItem.id)

        postRef.set(purchaseItem)
            .addOnFailureListener {
                isUpdationFailed = true
                failureMessage = it.stackTraceToString()
            }
            .await()

        // Emit success state with post reference
        emit(State.success(postRef))

        if (isUpdationFailed){
            // Emit Fail state with message
            emit(State.failed(failureMessage))
        }


    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Deletes selected purchaseItem with given documentId [documentId] from the cloud firestore collection.
     * @return The Flow of [State] which will store state of current action.
     */
    fun deleteCurrentItemOfCurrentMonth(documentId: String) = flow<State<DocumentReference>> {

        var  failureMessage = ""
        var isDeletionFailed = false

        // Emit loading state
        emit(State.loading())

        val postRef = mCurrentMonthPurchaseItemsCollection
            .document(documentId)

        // Just Delete current selected item
        postRef.delete()
            .addOnFailureListener {
                isDeletionFailed = true
                failureMessage = it.stackTraceToString()
            }.await()

        // Emit success state with post reference
        emit(State.success(postRef))

        if (isDeletionFailed){
            // Emit Fail state with message
            emit(State.failed(failureMessage))
        }

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


}