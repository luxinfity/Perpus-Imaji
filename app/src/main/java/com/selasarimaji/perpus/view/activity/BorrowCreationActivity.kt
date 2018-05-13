package com.selasarimaji.perpus.view.activity

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_borrow.*
import java.util.*


class BorrowCreationActivity : BaseContentCreationActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditBorrowVM::class.java)
    }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_borrow, null)
        linearContainer.addView(view, 0)

        borrowStartDateInputLayout.editText?.run { showDatePickerOnClick(this) }
        borrowEndDateInputLayout.editText?.run { showDatePickerOnClick(this) }
    }

    override fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Peminjaman"
    }

    override fun setupObserver(){
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

    override fun submitValue() {
        val bookName = borrowBookInputLayout.editText?.text.toString()
        val borrower = borrowNameInputLayout.editText?.text.toString()
        val startDate = borrowStartDateInputLayout.editText?.text.toString()
        val endDate = borrowEndDateInputLayout.editText?.text.toString()

        viewModel.storeData(DataModel.Borrow(bookName, borrower, startDate, endDate))
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
}
