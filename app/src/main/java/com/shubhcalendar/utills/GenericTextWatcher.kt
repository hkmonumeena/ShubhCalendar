package com.shubhcalendar.utills

import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import com.shubhcalendar.R

class GenericTextWatcher internal constructor(val editText: Array<EditText>, var view: View) :
    TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable) {
        // TODO Auto-generated method stub
        val text = editable.toString()
        when (view.id) {
            R.id.etOne -> if (text.length == 1) editText[1].requestFocus()
            R.id.etTwo -> if (text.length == 1) editText[2].requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
            R.id.etThree -> if (text.length == 1) editText[3].requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
            R.id.etFour -> if (text.isEmpty()) editText[2].requestFocus()
        }
    }
}