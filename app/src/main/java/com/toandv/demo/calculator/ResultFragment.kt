package com.toandv.demo.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import kotlinx.android.synthetic.main.fragment_result.*
import org.mariuszgromada.math.mxparser.Expression
import java.math.MathContext


class ResultFragment : Fragment(), FragmentResultListener {

    private var input = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input = savedInstanceState?.getString(BUNDLE_INPUT_STATE) ?: ""
        parentFragmentManager.setFragmentResultListener(REQUEST_KEYBOARD, this, this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_INPUT_STATE, input)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {

        when (val key = result.getCharSequence(BUNDLE_INPUT)) {
            getString(R.string.key_backspace) -> input = input.dropLast(1)
            getString(R.string.key_equal) -> calculator()
            else -> input = input.plus(key)
        }

        display()
    }

    private fun display() {
        input = input.replace("([+×÷-])\\1+".toRegex(), "$1")
        edtInput.setText(input)
        edtInput.setSelection(input.length)
    }

    private fun calculator() {

        val expression = Expression(
            input
                .replace(getString(R.string.key_multiplied), "*")
                .replace(getString(R.string.key_divided), "/")
        )

        when {
            input.isBlank() -> edtResult.setText(R.string.default_result)
            expression.checkSyntax() -> edtResult.setText(
                expression.calculate().toBigDecimal(MathContext.DECIMAL64).toPlainString()
            )
            else -> edtResult.setText(R.string.err_expression_invalid)
        }
    }

    companion object {
        fun newInstance() = ResultFragment()
    }
}