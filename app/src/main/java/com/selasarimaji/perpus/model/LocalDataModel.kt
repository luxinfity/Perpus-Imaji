package com.selasarimaji.perpus.model

data class RepoImage (val imagePath: String,
                      val isRemoteSource: Boolean)

enum class LoadingType{
    Create, Read, Update, Delete
}

fun getLoadingTypeText(loadingType: LoadingType) = when(loadingType){
    LoadingType.Create -> "Penambahan Berhasil"
    LoadingType.Read -> "Load Sukses"
    LoadingType.Update -> "Perubahan Disimpan"
    LoadingType.Delete -> "Data Berhasil Dihapus"
}

data class LoadingProcess(val isLoading: Boolean,
                          val isSuccess: Boolean,
                          val loadingType: LoadingType)