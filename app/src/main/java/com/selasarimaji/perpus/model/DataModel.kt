package com.selasarimaji.perpus.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot

abstract class DataModel {
    @get:Exclude var id: String? = ""
    @Exclude open val collectionName = ""

    data class Category (val name: String, val description: String,
                         val idParent: String? = "") : DataModel() {
        override val collectionName: String
            get() = "Category"

        companion object {
            fun turnDocumentToObject(documentSnapshot: QueryDocumentSnapshot) : Category {
                documentSnapshot.data.let {
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
                     val idCategory: String? = "") : DataModel(){
        override val collectionName: String
            get() = "Book"

        companion object {
            fun turnDocumentToObject(documentSnapshot: QueryDocumentSnapshot) : Book {
                documentSnapshot.data.let {
                    val name = it["name"].toString()
                    val author = it["author"].toString()
                    val year = it["year"].toString().toInt()
                    val publisher = it["publisher"].toString()
                    val idCategory = it["idCategory"]?.toString()
                    return Book(name, author, year, publisher, idCategory).apply {
                        id = documentSnapshot.id
                    }
                }
            }
        }
    }
    data class Borrow (val idBook: String, val idChild: String,
                       val startDate: String, val endDate: String) : DataModel(){
        override val collectionName: String
            get() = "Borrow"

        companion object {
            fun turnDocumentToObject(documentSnapshot: QueryDocumentSnapshot) : Borrow {
                documentSnapshot.data.let {
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
                       val birthDate: String) : DataModel(){
        override val collectionName: String
            get() = "Kid"

        companion object {
            fun turnDocumentToObject(documentSnapshot: QueryDocumentSnapshot) : Kid {
                documentSnapshot.data.let {
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
