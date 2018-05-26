package com.selasarimaji.perpus.view.fragment.content.create

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.TextInputLayout
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.capitalizeWords
import com.selasarimaji.perpus.getCurrentDateString
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.BorrowVM
import kotlinx.android.synthetic.main.layout_content_creation.*
import kotlinx.android.synthetic.main.content_borrow.*
import java.util.*
import java.util.concurrent.TimeUnit

class BorrowCreationFragment : BaseCreationFragment() {

    override val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(BorrowVM::class.java)
    }

    private val kidText : String
        get() = borrowNameInputLayout.editText?.text.toString()

    private val bookText : String
        get() = borrowBookInputLayout.editText?.text.toString()

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_borrow, null)
        linearContainer.addView(view, 0)

        borrowStartDateInputLayout.editText?.run {
            setText(getCurrentDateString(0))
            showDatePickerOnClick(this, 0)
        }
        borrowEndDateInputLayout.editText?.run {
            setText(getCurrentDateString(3))
            showDatePickerOnClick(this, 3)
        }

        borrowNameInputLayout.editText?.let {
            RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.getPossibleKidName(it)
                    }
        }

        borrowBookInputLayout.editText?.let {
            RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.getPossibleBookName(it)
                    }
        }
    }

    override fun setupToolbar(){
        viewModel.title.value = "Peminjaman"
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
        viewModel.filteredKid.observe(this, Observer {
            it?.run {
                val adapter = ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        this.filter { it.name.contains(kidText) }.map { it.name.capitalizeWords() })
                (borrowNameInputLayout.editText as AutoCompleteTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
        viewModel.filteredBook.observe(this, Observer {
            it?.run {
                val adapter = ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        this.filter { it.name.contains(bookText) }.map { it.name.capitalizeWords() })
                (borrowBookInputLayout.editText as AutoCompleteTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
    }

    override fun submitValue() {
        val editTextList = arrayListOf<TextInputLayout>(borrowBookInputLayout,
                borrowNameInputLayout, borrowStartDateInputLayout,
                borrowEndDateInputLayout).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }
        val bookName = viewModel.filteredBook.value?.find {
                it.name == bookText.toLowerCase()
            }?.id.also {
                if (!it.isNullOrEmpty()) {
                    editTextList.remove(borrowBookInputLayout)
                }
            }

        val borrower = viewModel.filteredKid.value?.find {
                it.name == kidText.toLowerCase()
            }?.id.also {
                if (!it.isNullOrEmpty()) {
                    editTextList.remove(borrowNameInputLayout)
                }
            }
        val startDate = borrowStartDateInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(borrowStartDateInputLayout)
            }
        }
        val endDate = borrowEndDateInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(borrowEndDateInputLayout)
            }
        }

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        if(editTextList.isEmpty()) {
            viewModel.storeData(DataModel.Borrow(bookName!!, borrower!!, startDate, endDate))
        }
    }

    private fun showDatePickerOnClick(editText: EditText, dayAhead : Int){
        val c = Calendar.getInstance().apply {
            add(Calendar.DATE, dayAhead)
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        editText.setOnClickListener {
            DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        editText.setText("$month/$day/$year")
                    },
                    year, month, day).show()
        }
    }
}
