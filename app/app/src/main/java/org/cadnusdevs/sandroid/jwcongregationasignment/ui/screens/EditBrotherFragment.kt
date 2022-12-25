package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.Button
import org.cadnusdevs.sandroid.jwcongregationasignment.*
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment

class EditBrotherFragment : BaseFragment() {
    private var enabledEditMode: Boolean = false
    private lateinit var repository: BrotherRepository
    private var brotherId: Long? = null

    override fun getTemplate() = R.layout.fragment_edit_brother
    override fun configureLayout(view: View?) {
        this.repository = BrotherRepository(requireActivity())
    }

    override fun setViewData() {
        arguments?.let {
            brotherId = it.getLong(UNIQUE_BROTHER_KEY)
        }
    }

    private fun fillFormFields(brother: Brother) {
        this.q.makeReadOnly(R.id.editTextBrotherName)
        this.q.setText(R.id.editTextBrotherName, brother.name)
        this.q.setBool(R.id.checkBoxUsher, brother.canBeUsher)
        this.q.setBool(R.id.checkBoxMicrophone, brother.canBeMicrophone)
        this.q.setBool(R.id.checkBoxComputer, brother.canBeComputer)
        this.q.setBool(R.id.checkBoxSoundSystem, brother.canBeSoundSystem)
    }

    override fun setEvents() {
        var brother = repository.select { x-> x.id == brotherId }
        enabledEditMode = brother != null
        q.find<Button>(R.id.delete_brother_button)?.visibility = if(enabledEditMode) View.VISIBLE else View.GONE

        if(brother != null) {
            this.fillFormFields(brother)
        }

        setSaveButtonEvents()
        setDeleteButtonEvents()
    }

    private fun setDeleteButtonEvents() {
        this.q.onClick(R.id.delete_brother_button) {
            val brother = repository.select { x-> x.id == brotherId }
            brother?.let { it1 -> repository.delete(it1) }
            this.goBack()
        }
        q.showIf(R.id.delete_brother_button, enabledEditMode)
    }

    private fun setSaveButtonEvents() {
        this.q.onClick(R.id.buttonSave) {
            var brother = Brother(
                this.brotherId?:0,
                this.q.getEditTextValue(R.id.editTextBrotherName),
                this.q.checkBoxValue(R.id.checkBoxUsher),
                this.q.checkBoxValue(R.id.checkBoxMicrophone),
                this.q.checkBoxValue(R.id.checkBoxComputer),
                this.q.checkBoxValue(R.id.checkBoxSoundSystem))

            if(brother.satisfyRules()) {
                if(this.enabledEditMode) {
                    repository.update(brother)
                }else{
                    repository.insert(brother)
                }
                this.goBack()
            } else this.showToast(BROTHER_ERROR_MSG)
        }
    }

    companion object {
        fun newInstance(brother: Brother? = null) =
            EditBrotherFragment().apply {
                arguments = Bundle().apply {
                    putLong(UNIQUE_BROTHER_KEY, brother?.id?:0)
                }
            }
    }
}