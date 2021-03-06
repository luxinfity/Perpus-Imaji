package com.selasarimaji.perpus.view.fragment.content

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.capitalizeWords
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.tryToRemoveFromList
import com.selasarimaji.perpus.viewmodel.content.CategoryVM
import kotlinx.android.synthetic.main.layout_content_creation.*
import kotlinx.android.synthetic.main.content_category.*
import java.util.concurrent.TimeUnit

class CategoryInspectFragment : BaseInspectFragment<RepoDataModel.Category>() {
    override val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(CategoryVM::class.java)
    }

    private val parentCategoryText : String
        get() = categoryParentInputLayout.editText?.text.toString()

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_category, null)
        linearContainer.addView(view, 0)
        categoryPathInputLayout.visibility = View.GONE
        categoryParentInputLayout.editText?.let{
            RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.getPossibleCategoryInputName(it)
                    }
        }
    }

    override fun setupToolbar(){
        viewModel.title.value = "Kategori"
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as RepoDataModel.Category?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            }
        })
    }

    override fun setupObserver(){
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as RepoDataModel.Category?)?.let {
                categoryNameInputLayout.editText?.setText(it.name)
                categoryCodeInputLayout.editText?.setText(it.code)
                categoryDescInputLayout.editText?.setText(it.description)
                categoryParentInputLayout.editText?.setText(it.idParent)
                categoryPathInputLayout.editText?.setText(it.idParent)
            }
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            addButton.visibility = if (it?.second != true) View.GONE else View.VISIBLE
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            arrayListOf<TextInputLayout>(categoryNameInputLayout,
                    categoryCodeInputLayout,
                    categoryDescInputLayout,
                    categoryParentInputLayout,
                    categoryPathInputLayout)
                    .apply {
                        this.map {
                            it.editText?.inputType = InputType.TYPE_NULL
                        }
                    }
                    .apply {
                        if (it?.first != true) {
                            this.map {
                                it.editText?.inputType = InputType.TYPE_NULL
                            }
                        } else {
                            this.map {
                                it.editText?.inputType = InputType.TYPE_CLASS_TEXT
                            }
                        }
                    }
        })
        viewModel.isLoading.observe(this, Observer {
            addButton.isEnabled = !(it ?: false)
        })
        viewModel.shouldFinish.observe(this, Observer {
            if (it == true){
                activity?.let {
                    it.setResult(Activity.RESULT_OK)
                    it.finish()
                }
            }
        })
        viewModel.repoCategoryVal.fetchedData.observe(this, Observer {
            it?.run {
                val adapter = ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        this.filter { it.name.contains(parentCategoryText) }.map { it.name.capitalizeWords() })
                (categoryParentInputLayout.editText as AutoCompleteTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
    }

    override fun createValue(): RepoDataModel.Category? {
        val editTextList = arrayListOf<TextInputLayout>(categoryNameInputLayout,
                categoryCodeInputLayout,
                categoryDescInputLayout).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }

        val name = categoryNameInputLayout.tryToRemoveFromList(editTextList)
        val code = categoryCodeInputLayout.tryToRemoveFromList(editTextList)
        val desc = categoryDescInputLayout.tryToRemoveFromList(editTextList)

        val parent = viewModel.repoCategoryVal.fetchedData.value?.find {
            it.name == parentCategoryText.toLowerCase()
        }?.id ?: ""

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        return if(editTextList.isEmpty())
            RepoDataModel.Category(name, code, desc, parent)
        else
            null
    }

    override fun submitValue() {
        createValue()?.let {
            viewModel.storeData(it){
                if(it.isSuccess) {
                    showLoadingResultToast(it.loadingType)
                    viewModel.shouldFinish.value = true
                } else {
                    showErrorConnectionToast()
                }
            }
        }
    }

    override fun focusFirstText() {
        categoryNameInputLayout.requestFocus()
        (context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?)?.
                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun clearFocus() {
        (context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?)?.
                hideSoftInputFromWindow(linearContainer.windowToken, 0)
        viewModelInspect.setSelectedItem(viewModelInspect.getSelectedItemLiveData().value)
    }

    override fun tryDeleteCurrentItem() {
        AlertDialog.Builder(context).setTitle("Are you sure want to delete")
                .setPositiveButton("Yes"){ dialog, _ ->
                    super.tryDeleteCurrentItem()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun deleteCurrentItem() {
        viewModelInspect.getSelectedItemLiveData().value?.run{
            viewModel.canSafelyDeleted(id){
                when (it.data) {
                    true -> {
                        viewModel.deleteCurrent(this){
                            if (it.isSuccess) {
                                showLoadingResultToast(it.loadingType)
                                viewModel.shouldFinish.value = true
                            } else{
                                showErrorConnectionToast()
                            }
                        }
                        viewModelInspect.editOrCreateMode.value = Pair(false, false)
                    }
                    null -> Toast.makeText(context,
                            "Gagal, Jaringan terganggu, silahkan coba lagi",
                            Toast.LENGTH_SHORT).show()
                    else -> Snackbar.make(linearContainer,
                            "Item ini masih digunakan oleh item lain, edit atau hapus item tersebut terlebih dahulu",
                            Snackbar.LENGTH_INDEFINITE).run {
                                setAction("OK"){
                                    dismiss()
                                }
                            }.show()
                }
            }
        }
    }

    override fun tryUpdateCurrentItem() {
        AlertDialog.Builder(context).setTitle("Are you sure want to update?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    super.tryUpdateCurrentItem()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun updateCurrentItem() {
        createValue()?.let {
            viewModel.updateData(it.apply {
                id = viewModelInspect.getSelectedItemLiveData().value!!.id
            }){
                if (it.isSuccess) {
                    showLoadingResultToast(it.loadingType)
                    viewModel.shouldFinish.value = true
                } else{
                    showErrorConnectionToast()
                }
            }
            viewModelInspect.editOrCreateMode.value = Pair(false, false)
        }
    }
}
