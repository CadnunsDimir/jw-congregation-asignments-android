package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.os.Bundle
import android.view.View
import org.cadnusdevs.sandroid.jwcongregationasignment.ARG_PARAM1
import org.cadnusdevs.sandroid.jwcongregationasignment.ARG_PARAM2
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.db
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment

class NewBrotherFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    override fun getTemplate() = R.layout.fragment_new_brother
    override fun configureLayoutManager(view: View?) {
    }

    override fun setViewData() {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setEvents() {
        this.onClick(R.id.buttonSave) {
            var brother = Brother(
                this.getEditTextValue(R.id.editTextBrotherName),
                this.checkBoxValue(R.id.checkBoxUsher),
                this.checkBoxValue(R.id.checkBoxMicrophone),
                this.checkBoxValue(R.id.checkBoxComputer),
                this.checkBoxValue(R.id.checkBoxSoundSystem)
            )
            db.brothers.add(brother)
            this.back()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            NewBrotherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}