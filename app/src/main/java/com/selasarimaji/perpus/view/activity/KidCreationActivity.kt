package com.selasarimaji.perpus.view.activity

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditKidVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_kid.*
import java.util.*


class KidCreationActivity : BaseContentCreationActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditKidVM::class.java)
    }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_kid, null)
        linearContainer.addView(view, 0)

        kidBirthDateInputLayout.editText?.run { showDatePickerOnClick(this) }
    }

    override fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Anak"
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
        val name = kidNameInputLayout.editText?.text.toString()
        val address = kidAddressInputLayout.editText?.text.toString()
        val gender = kidGenderInputLayout.editText?.text.toString() == "Cowok"
        val dateOfBirth = kidBirthDateInputLayout.editText?.text.toString()

        viewModel.storeData(DataModel.Kid(name, address, gender, dateOfBirth))
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
