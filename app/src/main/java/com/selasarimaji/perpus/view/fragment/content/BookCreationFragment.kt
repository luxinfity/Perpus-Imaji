package com.selasarimaji.perpus.view.fragment.content

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.hootsuite.nachos.NachoTextView
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.capitalizeWords
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditBookVM
import kotlinx.android.synthetic.main.activity_base_content_creation.*
import kotlinx.android.synthetic.main.content_book.*
import java.util.concurrent.TimeUnit

class BookCreationFragment : BaseCreationFragment() {

    override val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(EditBookVM::class.java)
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
            ImagePicker.create(this) // Activity or Fragment
                    .folderMode(true) // folder mode (false by default)
                    .toolbarFolderTitle("Folder") // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                    .showCamera(true) // show camera or not (true by default)
                    .start() // start image picker activity with request code
        }
    }

    override fun setupToolbar(){
        viewModel.title.value = "Buku"
    }

    override fun setupObserver(){
        viewModel.uploadingFlag.observe(this, Observer {
            it?.run {
                addButton.isEnabled = !this
            }
        })
        viewModel.uploadingSuccessFlag.observe(this, Observer {
            it?.run {
                if(this) {
                    Toast.makeText(context,
                            "Penambahan Berhasil",
                            Toast.LENGTH_SHORT).show()
                    activity?.let {
                        it.setResult(Activity.RESULT_OK)
                        it.finish()
                    }
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

        val name = bookNameInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(bookNameInputLayout)
            }
        }
        val author = bookAuthorInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(bookAuthorInputLayout)
            }
        }
        val year = yearInputLayout.editText?.text.toString().also {
            if (it.isNotEmpty()) {
                editTextList.remove(yearInputLayout)
            }
        }
        val publisher = publisherInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(publisherInputLayout)
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ImagePicker.getFirstImageOrNull(data)?.let {
                viewModel.imagePickActivityResult(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
