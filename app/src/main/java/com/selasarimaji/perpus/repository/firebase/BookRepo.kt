package com.selasarimaji.perpus.repository.firebase

import android.util.Log
import com.selasarimaji.perpus.model.DataModel
import com.google.firebase.firestore.DocumentChange



class BookRepo : BaseRepo<DataModel.Book>() {
    override val collectionName: String
        get() = "Book"

    override val TAG: String
        get() = BookRepo::javaClass.toString()

    override fun loadRangeInternal(startPosition: Int, loadCount: Int, isInitial:Boolean, listener: Any){
        db.orderBy("name").startAt(startPosition).limit(loadCount.toLong())
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "listen:error", firebaseFirestoreException)
                    }else {
                        for (dc in querySnapshot!!.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> Log.d(TAG, "New city: " + dc.document.data)
                                DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: " + dc.document.data)
                                DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: " + dc.document.data)
                            }
                        }
                    }
                }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DataModel.Book>) {
        loadRangeInternal(params.startPosition, params.loadSize, false, callback)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DataModel.Book>) {
        loadRangeInternal(params.requestedStartPosition, params.requestedLoadSize, true, callback)
    }
}