package com.selasarimaji.perpus.view.fragment.content

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.viewmodel.content.KidVM
import kotlinx.android.synthetic.main.layout_content_creation.*
import kotlinx.android.synthetic.main.content_kid.*
import java.util.*
import android.widget.ArrayAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.esafirm.imagepicker.features.ImagePicker
import com.selasarimaji.perpus.*
import com.selasarimaji.perpus.model.RepoImage

class KidInspectFragment : BaseInspectFragment<RepoDataModel.Kid>() {

    companion object {
        const val DoBKey = "KidCreationActivity-DoB"
    }

    override val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(KidVM::class.java)
    }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_kid, null)
        linearContainer.addView(view, 0)

        kidBirthDateInputLayout.editText?.run { showDatePickerOnClick(this) }
        val gender = arrayOf("Cowok", "Cewek")
        val adapter = ArrayAdapter<String>(context,
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
            clearFocus()
            ImagePicker.create(this).startImagePicker()
        }
    }

    override fun setupToolbar(){
        viewModel.title.value = "Anak"
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as RepoDataModel.Kid?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            }
        })
    }

    override fun setupObserver(){
        viewModelInspect.getSelectedItemLiveData().observe(this, Observer {
            (it as RepoDataModel.Kid?)?.let {
                kidNameInputLayout.editText?.setText(it.name)
                kidAddressInputLayout.editText?.setText(it.address)
                kidBirthDateInputLayout.editText?.setText(it.birthDate)
                kidGenderInputLayout.editText?.setText(if (it.isMale) "Cowok" else "Cewek")

                if (it.hasImage) {
                    viewModel.pickedImage.value = RepoImage(it.id, true)
                }
            }
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            // it?.second -> can be null
            addButton.visibility = if (it?.second != true) View.GONE else View.VISIBLE
            kidImageButton.isEnabled = it?.first == true
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            arrayListOf<TextInputLayout>(kidNameInputLayout,
                    kidAddressInputLayout,
                    kidBirthDateInputLayout,
                    kidGenderInputLayout)
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
                        this[2].isEnabled = it?.first == true
                    }
        })
        viewModel.isLoading.observe(this, Observer {
            addButton.isEnabled = !(it ?: false)
            kidImageButton.isEnabled = !(it ?: false)
        })
        viewModel.shouldFinish.observe(this, Observer {
            if (it == true){
                activity?.let {
                    it.setResult(Activity.RESULT_OK)
                    it.finish()
                }
            }
        })
        viewModel.pickedImage.observe(this, Observer {
            it?.run {
                context?.let {
                    GlideApp.with(it)
                            .load(if (!isRemoteSource) imagePath else viewModel.repo.getImageFull(imagePath))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_people.resDrawable(it).apply {
                                this?.tint(R.color.colorAccent.resColor(it))
                            })
                            .into(kidImageButton)
                }
            }
        })
    }

    override fun createValue(): RepoDataModel.Kid? {
        val editTextList = arrayListOf<TextInputLayout>(kidNameInputLayout, kidAddressInputLayout,
                kidBirthDateInputLayout).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }

        val name = kidNameInputLayout.tryToRemoveFromList(editTextList)
        val address = kidAddressInputLayout.tryToRemoveFromList(editTextList)
        val gender = kidGenderInputLayout.editText?.text.toString() == "Cowok"
        val dateOfBirth = kidBirthDateInputLayout.tryToRemoveFromList(editTextList)
        val hasImage = viewModel.pickedImage.value?.isRemoteSource == false

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        return if(editTextList.isEmpty())
            RepoDataModel.Kid(name, address, gender, dateOfBirth, hasImage)
        else
            null
    }

    override fun submitValue() {
        createValue()?.let {
            viewModel.storeData(it){
                if (it.isSuccess && viewModel.userHasLocalImage && it.data != null){
                    viewModel.storeImage(viewModel.repo, it.data, viewModel.isLoading){
                        if (it.isSuccess) {
                            showLoadingResultToast(it.loadingType)
                            viewModel.shouldFinish.value = true
                        }
                    }
                } else if(it.isSuccess) {
                    showLoadingResultToast(it.loadingType)
                    viewModel.shouldFinish.value = true
                } else {
                    showErrorConnectionToast()
                }
            }
        }
    }

    private fun showDatePickerOnClick(editText: EditText){
        val savedString = context?.getStringVal(DoBKey, "") ?: ""
        var c = Calendar.getInstance()
        if (savedString.isNotEmpty()){
           c = parseDateString(savedString)
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        editText.setOnClickListener {
            DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        editText.setText("${(month+1).addZeroIfBelow10()}/${day.addZeroIfBelow10()}/$year")
                    },
                    year, month, day).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ImagePicker.getFirstImageOrNull(data)?.let {
                viewModel.imagePickActivityResult(RepoImage(it.path, false))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun focusFirstText() {
        kidNameInputLayout.requestFocus()
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.
                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun clearFocus() {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.
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
                            if (it.isSuccess && viewModel.userHasRemoteImage){
                                viewModel.deleteImage(viewModel.repo,
                                        id,
                                        viewModel.isLoading){
                                    if (it.isSuccess) {
                                        showLoadingResultToast(it.loadingType)
                                        viewModel.shouldFinish.value = true
                                    }
                                }
                            } else if (it.isSuccess) {
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
                if (it.isSuccess && viewModel.userHasLocalImage){
                    viewModel.storeImage(viewModel.repo,
                            viewModelInspect.getSelectedItemLiveData().value!!.id,
                            viewModel.isLoading){
                        if (it.isSuccess) {
                            showLoadingResultToast(it.loadingType)
                            viewModel.shouldFinish.value = true
                        }
                    }
                } else if(it.isSuccess) {
                    showLoadingResultToast(it.loadingType)
                    viewModel.shouldFinish.value = true
                } else {
                    showErrorConnectionToast()
                }
            }
            viewModelInspect.editOrCreateMode.value = Pair(false, false)
        }
    }
}
