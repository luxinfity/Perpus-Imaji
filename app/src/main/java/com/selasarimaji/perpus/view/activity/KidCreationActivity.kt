package com.selasarimaji.perpus.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditKidVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_kid.*
import java.util.*
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.selasarimaji.perpus.getStringVal
import com.selasarimaji.perpus.parseDateString
import com.selasarimaji.perpus.storeStringVal

class KidCreationActivity : BaseContentCreationActivity() {

    companion object {
        const val DoBKey = "KidCreationActivity-DoB"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditKidVM::class.java)
    }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_kid, null)
        linearContainer.addView(view, 0)

        kidBirthDateInputLayout.editText?.run { showDatePickerOnClick(this) }
        val gender = arrayOf("Cowok", "Cewek")
        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                gender)

        (kidGenderInputLayout.editText as AutoCompleteTextView).run {
            setAdapter(adapter)
            setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    (kidGenderInputLayout.editText as AutoCompleteTextView).showDropDown()
                }
            }
        }

        kidImageButton.setOnClickListener {
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Anak"
    }

    override fun setupObserver(){
        viewModel.uploadingFlag.observe(this, Observer {
            it?.run {
                progressBar.visibility = if (this) View.VISIBLE else View.GONE
                addButton.isEnabled = !this
            }
        })
//        viewModel.uploadingProgress.observe(this, Observer {
//            it?.run {
//                progressBar.isIndeterminate = false
//                progressBar.progress = it.roundToInt()
//            }
//        })
        viewModel.uploadingSuccessFlag.observe(this, Observer {
            it?.run {
                if(this && !viewModel.shouldWaitImageUpload()) {
                    Toast.makeText(applicationContext,
                            "Penambahan Berhasil",
                            Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }else if (this) {
                    viewModel.storeImage()
                }
            }
        })
        viewModel.pickedImage.observe(this, Observer {
            it?.run {
                Glide.with(applicationContext).load(it.path).into(kidImageButton)
            }
        })
    }

    override fun submitValue() {
        val editTextList = arrayListOf<TextInputLayout>(kidNameInputLayout, kidAddressInputLayout,
                kidBirthDateInputLayout).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }

        val name = kidNameInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(kidNameInputLayout)
            }
        }
        val address = kidAddressInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(kidAddressInputLayout)
            }
        }
        val gender = kidGenderInputLayout.editText?.text.toString() == "Cowok"
        val dateOfBirth = kidBirthDateInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(kidBirthDateInputLayout)
                storeStringVal(DoBKey, it)
            }
        }

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        if(editTextList.isEmpty()) {
            viewModel.storeData(DataModel.Kid(name, address, gender, dateOfBirth))
        }
    }

    private fun showDatePickerOnClick(editText: EditText){
        val savedString = getStringVal(DoBKey, "")
        var c = Calendar.getInstance()
        if (savedString.isNotEmpty()){
           c = parseDateString(savedString)
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ImagePicker.getFirstImageOrNull(data)?.let {
                viewModel.imagePickActivityResult(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
