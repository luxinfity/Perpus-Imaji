package com.selasarimaji.perpus.view.fragment.content

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.hootsuite.nachos.NachoTextView
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.*
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.model.getLoadingTypeText
import com.selasarimaji.perpus.viewmodel.BookVM
import kotlinx.android.synthetic.main.content_book.*
import kotlinx.android.synthetic.main.layout_content_creation.*
import java.util.concurrent.TimeUnit

class BookInspectFragment : BaseInspectFragment() {

    override val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(BookVM::class.java)
    }

    private val parentCategoryText : String
        get() = (categoryListChipInput.editText as  NachoTextView).tokenValues.let {
            if (it.size > 0) it.last()
            else ""
        }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_book, null)
        linearContainer.addView(view, 0)
        (bookAuthorInputLayout.editText as NachoTextView).also {
            it.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL)
            it.enableEditChipOnTouch(false, false)
        }

        categoryListChipInput.editText?.let{
            RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.getPossibleCategoryInputName(parentCategoryText)
                    }
        }

        bookImageButton.setOnClickListener {
            ImagePicker.create(this).startImagePicker()
        }
    }

    override fun setupToolbar(){
        viewModel.title.value = "Buku"
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as RepoDataModel.Book?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            }
        })
    }

    override fun setupObserver(){
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as RepoDataModel.Book?)?.let {
                bookNameInputLayout.editText?.setText(it.name)
                bookAuthorInputLayout.editText?.setText(it.authors.toString())
                yearInputLayout.editText?.setText(it.year.toString())
                publisherInputLayout.editText?.setText(it.publisher)
                categoryListChipInput.editText?.setText(it.idCategoryList.toString())

                viewModel.pickedImage.value = RepoImage(it.id, true)
            }
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            addButton.visibility = if (it?.second != true) View.GONE else View.VISIBLE
            bookImageButton.isEnabled = it?.second == true
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            arrayListOf<TextInputLayout>(bookNameInputLayout,
                    bookAuthorInputLayout,
                    yearInputLayout,
                    publisherInputLayout,
                    categoryListChipInput)
                    .apply {
                        if (it?.first != true) {
                            this.map {
                                it.editText?.inputType = InputType.TYPE_NULL
                            }
                        } else {
                            this.map {
                                it.editText?.inputType = InputType.TYPE_CLASS_TEXT
                            }
                            this[2].editText?.inputType = InputType.TYPE_CLASS_NUMBER // year input
                        }
                    }
        })
        viewModel.loadingProcess.observe(this, Observer {
            it?.run {
                // loading bar
                addButton.isEnabled = !isLoading
                bookImageButton.isEnabled = !isLoading

                // loading process
                val userHasLocalImageToUpload = viewModel.pickedImage.value?.isRemoteSource ?: false
                when {
                    isSuccess -> {
                        if (userHasLocalImageToUpload){
                            viewModel.storeImage()
                        } else {
                            Toast.makeText(context,
                                    getLoadingTypeText(loadingType),
                                    Toast.LENGTH_SHORT).show()
                            activity?.let {
                                it.setResult(Activity.RESULT_OK)
                                it.finish()
                            }
                        }
                    }
                    !isSuccess && !isLoading -> {
                        Toast.makeText(context,
                                "Gagal, Jaringan terganggu, silahkan coba lagi",
                                Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        })
        viewModel.repoCategoryVal.fetchedData.observe(this, Observer {
            it?.run {
                val adapter = ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        this.filter { it.name.contains(parentCategoryText) }
                                .map { it.name.capitalizeWords() })
                (categoryListChipInput.editText as NachoTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
        viewModel.pickedImage.observe(this, Observer {
            it?.run {
                context?.let {
                    GlideApp.with(it)
                            .load(this.imagePath)
                            .placeholder(R.drawable.ic_camera.resDrawable(it))
                            .into(bookImageButton)
                }
            }
        })
    }

    override fun submitValue() {
        val editTextList = arrayListOf<TextInputLayout>(bookNameInputLayout,
                bookAuthorInputLayout, yearInputLayout,
                publisherInputLayout, categoryListChipInput).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }

        val name = bookNameInputLayout.tryToRemoveFromList(editTextList)
        val authors = (bookAuthorInputLayout.editText as NachoTextView).chipValues.map {
            it.toLowerCase()
        }.also {
            if (it.isNotEmpty()) {
                editTextList.remove(bookAuthorInputLayout)
            }
        }
        val year = yearInputLayout.tryToRemoveFromList(editTextList)
        val publisher = publisherInputLayout.tryToRemoveFromList(editTextList)
        val category = (categoryListChipInput.editText as NachoTextView).chipValues.map {
            val value = it
            viewModel.repoCategoryVal.fetchedData.value?.
                    find { it.name == value.toLowerCase() }?.id
                    ?: ""
        }.also {
            if (it.isNotEmpty()) {
                editTextList.remove(categoryListChipInput)
            }
        }

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        if(editTextList.isEmpty()){
            viewModel.storeData(RepoDataModel.Book(name, authors, year.toInt(), publisher, category))
        }
    }

    override fun focusFirstText() {
        bookNameInputLayout.requestFocus()
        (context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?)?.
                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun clearFocus() {
        (context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?)?.
                hideSoftInputFromWindow(linearContainer.windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ImagePicker.getFirstImageOrNull(data)?.let {
                viewModel.imagePickActivityResult(RepoImage(it.path, false))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
        viewModelInspect.getSelectedItemLiveData().value?.let {
            viewModel.deleteCurrent(it)
        }
    }
}
