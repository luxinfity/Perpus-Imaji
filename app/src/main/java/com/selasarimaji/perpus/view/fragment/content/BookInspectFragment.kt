package com.selasarimaji.perpus.view.fragment.content

import android.app.Activity
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
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.hootsuite.nachos.NachoTextView
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.*
import com.selasarimaji.perpus.model.DataModel
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
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Book?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            } ?: also {
                viewModel.title.value = "Buku"
            }
        })
    }

    override fun setupObserver(){
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Book?)?.let {
                bookNameInputLayout.editText?.setText(it.name)
                bookAuthorInputLayout.editText?.setText(it.author)
                yearInputLayout.editText?.setText(it.year.toString())
                publisherInputLayout.editText?.setText(it.publisher)
                categoryListChipInput.editText?.setText(it.idCategoryList.toString())

                context?.run {
                    GlideApp.with(this)
                            .load(viewModel.repo.getImageFull(it.id))
                            .placeholder(R.drawable.ic_camera.resDrawable(this))
                            .into(bookImageButton)
                }
            }
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            addButton.visibility = if (it?.second != true) View.GONE else View.VISIBLE
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
                        }
                        this[2].editText?.inputType = InputType.TYPE_CLASS_NUMBER // year input
                    }
        })

        viewModel.uploadingFlag.observe(this, Observer {
            it?.run {
                progressBar.visibility = if (this) View.VISIBLE else View.GONE
                addButton.isEnabled = !this
            }
        })
        viewModel.uploadingSuccessFlag.observe(this, Observer {
            it?.also {
                if(it && !viewModel.shouldWaitImageUpload()) {
                    Toast.makeText(context,
                            "Penambahan Berhasil",
                            Toast.LENGTH_SHORT).show()
                    activity?.let {
                        it.setResult(Activity.RESULT_OK)
                        it.finish()
                    }
                }else if (it) {
                    viewModel.storeImage()
                }
            }
        })
        viewModel.filteredCategory.observe(this, Observer {
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
                    Glide.with(it).load(this.path).into(bookImageButton)
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
        val author = bookAuthorInputLayout.tryToRemoveFromList(editTextList)
        val year = yearInputLayout.tryToRemoveFromList(editTextList)
        val publisher = publisherInputLayout.tryToRemoveFromList(editTextList)
        val category = (categoryListChipInput.editText as NachoTextView).chipValues.map {
            val value = it
            viewModel.filteredCategory.value?.
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
            viewModel.storeData(DataModel.Book(name, author, year.toInt(), publisher, category))
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
                viewModel.imagePickActivityResult(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
