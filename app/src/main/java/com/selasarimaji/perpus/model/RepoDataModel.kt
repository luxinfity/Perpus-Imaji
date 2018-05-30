package com.selasarimaji.perpus.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*

abstract class RepoDataModel : Serializable {
    @get:Exclude var id: String = ""
    @get:Exclude abstract val collectionName : String

    // region data tracker
    // don't remove, will be used in serializing
    val editor : String
        get() = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val lastEdit : Long
        get() = Calendar.getInstance().time.time
    // endregion

    data class Category (val name: String, val description: String,
                         val idParent: String? = "") : RepoDataModel() {
        @get:Exclude override val collectionName = "Category"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Category {
                documentSnapshot.let {
                    val name = it["name"].toString()
                    val description = it["description"].toString()
                    val idParent = it["idParent"].toString()
                    return Category(name, description, idParent).apply {
                        id = documentSnapshot.id
                    }
                }
            }
        }
    }
    data class Book (val name: String, val author: String, val year: Int, val publisher: String,
                     val idCategoryList: List<String>) : RepoDataModel(){
        @get:Exclude override val collectionName = "Book"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Book {
                documentSnapshot.let {
                    val name = it["name"].toString()
                    val author = it["author"].toString()
                    val year = it["year"].toString().toInt()
                    val publisher = it["publisher"].toString()
                    val idCategoryList = it["idCategoryList"]?.let {
                        (it as Iterable<*>).map{ it.toString() }
                    } ?: listOf()

                    return Book(name, author, year, publisher, idCategoryList).apply {
                        id = documentSnapshot.id
                    }
                }
            }
        }
    }
    data class Borrow (val idBook: String, val idChild: String,
                       val startDate: String, val endDate: String) : RepoDataModel(){
        @get:Exclude override val collectionName = "Borrow"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Borrow {
                documentSnapshot.let {
                    val idBook = it["idBook"].toString()
                    val idChild = it["idChild"].toString()
                    val startDate = it["startDate"].toString()
                    val endDate = it["endDate"].toString()
                    return Borrow(idBook, idChild, startDate, endDate).apply {
                        id = documentSnapshot.id
                    }
                }
            }
        }
    }
    data class Kid (val name: String, val address: String, val isMale: Boolean,
                       val birthDate: String) : RepoDataModel(){
        @get:Exclude override val collectionName = "Kid"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Kid {
                documentSnapshot.let {
                    val name = it["name"].toString()
                    val address = it["address"].toString()
                    val isMale = it["male"].toString().toBoolean()
                    val birthDate = it["birthDate"].toString()
                    return Kid(name, address, isMale, birthDate).apply {
                        id = documentSnapshot.id
                    }
                }
            }
        }
    }
}
