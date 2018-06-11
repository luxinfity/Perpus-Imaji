package com.selasarimaji.perpus.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.gson.JsonObject
import java.io.Serializable
import java.util.*

abstract class RepoDataModel : Serializable {
    @get:Exclude var id: String = ""
    @get:Exclude abstract val collectionName : String

    // region data tracker
    // don't remove, will be used in serializing
    @Suppress("unused")
    val editor : String
        get() = FirebaseAuth.getInstance().currentUser?.email ?: ""
    @Suppress("unused")
    val lastEdit : Long
        get() = Calendar.getInstance().time.time
    // endregion

    data class Category (val name: String,
                         val description: String,
                         val idParent: String = "") : RepoDataModel() {
        @get:Exclude override val collectionName = "Category"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Category {
                documentSnapshot.let {
                    val name = it["name"].toString()
                    val description = it["description"].toString()
                    val idParent = it["idParent"]?.toString() ?: ""
                    return Category(name, description, idParent).apply {
                        id = documentSnapshot.id
                    }
                }
            }
            fun turnDocumentToObject(jsonObject: JsonObject) : Category {
                jsonObject.let {
                    val name = it["name"].toString().removeSurrounding("\"")
                    val description = it["description"].toString().removeSurrounding("\"")
                    val idParent = it["idParent"]?.toString()?.removeSurrounding("\"") ?: ""
                    return Category(name, description, idParent).apply {
                        id = it["id"].toString().removeSurrounding("\"")
                    }
                }
            }
        }
    }
    data class Book (val name: String,
                     val authors: List<String>,
                     val year: Int,
                     val publisher: String,
                     val idCategoryList: List<String>,
                     val hasImage: Boolean) : RepoDataModel(){
        @get:Exclude override val collectionName = "Book"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Book {
                documentSnapshot.let {
                    val name = it["name"].toString()
                    val author = it["authors"].let {
                        (it as Iterable<*>).map{ it.toString() }
                    }
                    val year = it["year"].toString().toInt()
                    val publisher = it["publisher"].toString()
                    val idCategoryList = it["idCategoryList"]?.let {
                        (it as Iterable<*>).map{ it.toString() }
                    } ?: listOf()
                    val hasImage = it["hasImage"].toString().toBoolean()
                    return Book(name, author, year, publisher, idCategoryList, hasImage).apply {
                        id = documentSnapshot.id
                    }
                }
            }
            fun turnDocumentToObject(jsonObject: JsonObject) : Book {
                jsonObject.let {
                    val name = it["name"].toString().removeSurrounding("\"")
                    val author = it["authors"].let {
                        (it as Iterable<*>)
                                .map{ it.toString().removeSurrounding("\"") }
                    }
                    val year = it["year"].toString().removeSurrounding("\"").toInt()
                    val publisher = it["publisher"].toString().removeSurrounding("\"")
                    val idCategoryList = it["idCategoryList"]?.let {
                        (it as Iterable<*>).map{ it.toString().removeSurrounding("\"") }
                    } ?: listOf()
                    val hasImage = it["hasImage"].toString().removeSurrounding("\"").toBoolean()
                    return Book(name, author, year, publisher, idCategoryList, hasImage).apply {
                        id = it["id"].toString()
                    }
                }
            }
        }
    }
    data class Borrow (val idBook: String,
                       val idChild: String,
                       val startDate: String,
                       val endDate: String) : RepoDataModel(){
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
            fun turnDocumentToObject(jsonObject: JsonObject) : Borrow {
                jsonObject.let {
                    val idBook = it["idBook"].toString().removeSurrounding("\"")
                    val idChild = it["idChild"].toString().removeSurrounding("\"")
                    val startDate = it["startDate"].toString().removeSurrounding("\"")
                    val endDate = it["endDate"].toString().removeSurrounding("\"")
                    return Borrow(idBook, idChild, startDate, endDate).apply {
                        id = it["id"].toString().removeSurrounding("\"")
                    }
                }
            }
        }
    }
    data class Kid (val name: String,
                    val address: String,
                    val isMale: Boolean,
                    val birthDate: String,
                    val hasImage: Boolean) : RepoDataModel(){
        @get:Exclude override val collectionName = "Kid"

        companion object {
            fun turnDocumentToObject(documentSnapshot: DocumentSnapshot) : Kid {
                documentSnapshot.let {
                    val name = it["name"].toString()
                    val address = it["address"].toString()
                    val isMale = it["male"].toString().toBoolean()
                    val birthDate = it["birthDate"].toString()
                    val hasImage = it["hasImage"].toString().toBoolean()
                    return Kid(name, address, isMale, birthDate, hasImage).apply {
                        id = documentSnapshot.id
                    }
                }
            }
            fun turnDocumentToObject(jsonObject: JsonObject) : Kid {
                jsonObject.let {
                    val name = it["name"].toString().removeSurrounding("\"")
                    val address = it["address"].toString().removeSurrounding("\"")
                    val isMale = it["male"].toString().removeSurrounding("\"").toBoolean()
                    val birthDate = it["birthDate"].toString().removeSurrounding("\"")
                    val hasImage = it["hasImage"].toString().removeSurrounding("\"").toBoolean()
                    return Kid(name, address, isMale, birthDate, hasImage).apply {
                        id = it["id"].toString().removeSurrounding("\"")
                    }
                }
            }
        }
    }
}
