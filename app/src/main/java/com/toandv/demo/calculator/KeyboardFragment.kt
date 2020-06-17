package com.toandv.demo.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_keyboard.*

class KeyboardFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (item in constraintKeyboard.children) {
            if (item is Button) item.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        if (view is Button) parentFragmentManager.setFragmentResult(
            REQUEST_KEYBOARD,
            bundleOf(BUNDLE_INPUT to view.text)
        )
    }

    companion object {
        fun newInstance() = KeyboardFragment()
    }

}
