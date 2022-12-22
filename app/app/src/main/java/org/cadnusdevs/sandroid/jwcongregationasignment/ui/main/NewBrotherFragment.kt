package org.cadnusdevs.sandroid.jwcongregationasignment.ui.main

import android.os.Bundle
import android.view.View
import org.cadnusdevs.sandroid.jwcongregationasignment.ARG_PARAM1
import org.cadnusdevs.sandroid.jwcongregationasignment.ARG_PARAM2
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.shared.BaseFragment

class NewBrotherFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    override fun getTemplate() = R.layout.fragment_new_brother
    override fun setViewData() {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setEvents() {
        this.onClick(R.id.buttonSave) {
            var brother = Brother()
            brother.name = this.getEditTextValue(R.id.editTextBrotherName)
            brother.canBeUsher = this.checkBoxValue(R.id.checkBoxUsher)
//            this.setText(R.id.textView, "Nome do irmão: ${brother.name}, Indicador: ${brother.canBeUsher}")
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