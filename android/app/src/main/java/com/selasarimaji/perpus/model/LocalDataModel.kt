package com.selasarimaji.perpus.model

data class RepoImage (val imagePath: String,
                      val isRemoteSource: Boolean)

class Loading{
    enum class Type{
        Create, Read, Update, Delete
    }
    companion object {
        fun getLoadingTypeText(loadingType: Type) = when(loadingType){
            Type.Create -> "Penambahan Berhasil"
            Type.Read -> "Load Sukses"
            Type.Update -> "Perubahan Disimpan"
            Type.Delete -> "Data Berhasil Dihapus"
        }
    }

    data class Result<T>(val isSuccess: Boolean,
                         val loadingType: Type,
                         val data: T? = null)

    data class Param(val loadPosition: Position = Position(),
                     val orderBy: String = "name",
                     val filterMap: Map<String, String>? = null){

        data class Position(val start: Int = -1, val stop: Int = -1)
    }
}