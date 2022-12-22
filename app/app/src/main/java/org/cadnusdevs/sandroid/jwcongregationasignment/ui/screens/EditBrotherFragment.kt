package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.os.Bundle
import android.view.View
import org.cadnusdevs.sandroid.jwcongregationasignment.*
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment

class EditBrotherFragment : BaseFragment() {
    private var enabledEditMode: Boolean = false
    private var repository: BrotherRepository = BrotherRepository()
    private var brotherName: String? = null

    override fun getTemplate() = R.layout.fragment_new_brother
    override fun configureLayoutManager(view: View?) {
    }

    override fun setViewData() {
        arguments?.let {
            brotherName = it.getString(UNIQUE_BROTHER_NAME_KEY)
        }
    }

    private fun fillFormFields(brother: Brother) {
        this.makeReadOnly(R.id.editTextBrotherName)
        this.setText(R.id.editTextBrotherName, brother.name)
        this.setBool(R.id.checkBoxUsher, brother.canBeUsher)
        this.setBool(R.id.checkBoxMicrophone, brother.canBeMicrophone)
        this.setBool(R.id.checkBoxComputer, brother.canBeComputer)
        this.setBool(R.id.checkBoxSoundSystem, brother.canBeSoundSystem)
    }

    override fun setEvents() {
        var brother = repository.select { x-> x.name == brotherName }
        enabledEditMode = brother != null
        if(brother != null) {
            this.fillFormFields(brother)
        }

        this.onClick(R.id.buttonSave) {
            var brother = Brother(
                this.getEditTextValue(R.id.editTextBrotherName),
                this.checkBoxValue(R.id.checkBoxUsher),
                this.checkBoxValue(R.id.checkBoxMicrophone),
                this.checkBoxValue(R.id.checkBoxComputer),
                this.checkBoxValue(R.id.checkBoxSoundSystem))

            if(brother.satisfyRules()) {
                if(this.enabledEditMode) {
                    repository.update(brother)
                }else{
                    repository.insert(brother)
                }
                this.goBack()
            }
            this.showToast(BROTHER_ERROR_MSG)
        }
    }

    companion object {
        fun newInstance(brotherName: String? = null) =
            EditBrotherFragment().apply {
                arguments = Bundle().apply {
                    putString(UNIQUE_BROTHER_NAME_KEY, brotherName)
                }
            }
    }
}