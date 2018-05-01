package com.selasarimaji.perpus.model

import com.google.firebase.firestore.Exclude

abstract class DataModel {
    @get:Exclude var id: String? = ""
    @get:Exclude open val collectionName = ""

    data class Category (val name: String, val description: String,
                         val idParent: String? = "") : DataModel() {
        override val collectionName: String
            get() = "Category"
    }
    data class Book (val name: String, val author: String, val year: Int, val publisher: String,
                     val idCategory: String? = "") : DataModel(){
        override val collectionName: String
            get() = "Book"
    }
    data class Borrow (val idBook: String, val idChild: String,
                       val startDate: String, val endDate: String) : DataModel(){
        override val collectionName: String
            get() = "Borrow"
    }
    data class Kid (val name: String, val address: String, val isMale: Boolean,
                       val birthDate: String) : DataModel(){
        override val collectionName: String
            get() = "Kid"
    }
}
