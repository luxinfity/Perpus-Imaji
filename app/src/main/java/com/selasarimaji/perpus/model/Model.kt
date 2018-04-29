package com.selasarimaji.perpus.model

import com.google.firebase.firestore.Exclude

class Model {
    data class Category (val name: String, val description: String,
                         val idParent: String? = "", @get:Exclude val id: String? = "")
}
