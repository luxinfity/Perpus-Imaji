package com.selasarimaji.perpus.view.activity

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditBookVM
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.EditKidVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_book.*
import kotlinx.android.synthetic.main.content_borrow.*
import kotlinx.android.synthetic.main.content_category.*
import kotlinx.android.synthetic.main.content_kid.*
import java.util.*

class ContentCreationActivity : BaseNavigationActivity() {

    private val contentType by lazy {
        intent.extras?.getSerializable(CONTENT_TYPE_KEY) ?: ContentType.Book
    }

    private val viewModel by lazy {
        when(contentType){
            ContentType.Category -> ViewModelProviders.of(this).get(EditCategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(this).get(EditBookVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(this).get(EditBorrowVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(this).get(EditKidVM::class.java)
            else -> ViewModelProviders.of(this).get(EditBookVM::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_creation)

        setupView()
        setupToolbar()
        setupDatePicker()
        setupObserver()
        addButton.setOnClickListener {
            when(contentType){
                ContentType.Category -> addCategory()
                ContentType.Book -> addBook()
                ContentType.Borrow -> addBorrow()
                ContentType.Kid -> addKid()
            }
        }
    }

    private fun setupView(){
        val layoutId = when(contentType){
            ContentType.Category -> R.layout.content_category
            ContentType.Book -> R.layout.content_book
            ContentType.Borrow -> R.layout.content_borrow
            ContentType.Kid -> R.layout.content_kid
            else -> R.layout.content_book
        }
        val view = layoutInflater.inflate(layoutId, null)
        linearContainer.addView(view, 0)
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${when(contentType){
            ContentType.Category -> "Kategori"
            ContentType.Book -> "Buku"
            ContentType.Borrow -> "Peminjaman"
            ContentType.Kid -> "Anak"
            else -> "Buku"
        }} Baru"
    }

    private fun setupDatePicker(){
        when(contentType){
            ContentType.Kid -> kidBirthDateInputLayout.editText?.run { showDatePickerOnClick(this) }
            ContentType.Borrow -> {
                borrowStartDateInputLayout.editText?.run { showDatePickerOnClick(this) }
                borrowEndDateInputLayout.editText?.run { showDatePickerOnClick(this) }
            }
        }
    }

    private fun setupObserver(){
        viewModel.uploadingFlag.observe(this, Observer<Boolean> {
            it?.run {
                progressBar.visibility = if (this) View.VISIBLE else View.GONE
                addButton.isEnabled = !this
            }
        })
        viewModel.uploadingSuccessFlag.observe(this, Observer<Boolean> {
            it?.run {
                if(this) {
                    Toast.makeText(applicationContext,
                            "Penambahan Berhasil",
                            Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })
    }

    private fun showDatePickerOnClick(editText: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        editText.setOnClickListener {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        editText.setText("$month/$day/$year")
                    },
                    year, month, day).show()
        }
    }

    private fun addCategory() {
        val name = categoryNameInputLayout.editText?.text.toString()
        val desc = categoryDescInputLayout.editText?.text.toString()
        val parent = categoryParentInputLayout.editText?.text.toString()

        (viewModel as EditCategoryVM).storeData(DataModel.Category(name, desc, parent))
    }

    private fun addBook() {
        val name = bookNameInputLayout.editText?.text.toString()
        val author = bookAuthorInputLayout.editText?.text.toString()
        val year = yearInputLayout.editText?.text.toString().toInt()
        val publisher = publisherInputLayout.editText?.text.toString()
        val category = categoryListChipInput.text.toString()

        (viewModel as EditBookVM).storeData(DataModel.Book(name, author, year, publisher, category))
    }

    private fun addBorrow() {
        val bookName = borrowBookInputLayout.editText?.text.toString()
        val borrower = borrowNameInputLayout.editText?.text.toString()
        val startDate = borrowStartDateInputLayout.editText?.text.toString()
        val endDate = borrowEndDateInputLayout.editText?.text.toString()

        (viewModel as EditBorrowVM).storeData(DataModel.Borrow(bookName, borrower, startDate, endDate))
    }

    private fun addKid() {
        val name = kidNameInputLayout.editText?.text.toString()
        val address = kidAddressInputLayout.editText?.text.toString()
        val gender = kidGenderInputLayout.editText?.text.toString() == "Cowok"
        val dateOfBirth = kidBirthDateInputLayout.editText?.text.toString()

        (viewModel as EditKidVM).storeData(DataModel.Kid(name, address, gender, dateOfBirth))
    }
}
