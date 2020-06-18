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

    private var inputText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputText = savedInstanceState?.getString(BUNDLE_INPUT_STATE) ?: ""
        parentFragmentManager.setFragmentResultListener(REQUEST_KEYBOARD, this, this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_INPUT_STATE, inputText)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {

        inputText = when (val key = result.getCharSequence(BUNDLE_INPUT)) {
            getString(R.string.key_equal) -> {

                val resultValue = edtResult.text.toString()
                val error = getString(R.string.err_expression_invalid)

                when {
                    resultValue.isBlank() || resultValue == error -> {
                        edtResult.setText(error)
                    }
                    else -> {
                        inputText = resultValue
                        edtInput.setText(resultValue)
                        edtInput.setSelection(inputText.length)
                    }
                }
                return
            }
            getString(R.string.key_backspace) -> inputText.dropLast(1)
            else -> inputText.plus(key)
        }

        calculator()
        edtInput.setText(inputText)
        edtInput.setSelection(inputText.length)
    }

    private fun calculator() {

        inputText = inputText.replace("([+×÷-])\\1+".toRegex(), "$1")

        val expression = Expression(
            inputText.replace(getString(R.string.key_multiplied), "*")
                .replace(getString(R.string.key_divided), "/")
                .replace(getString(R.string.key_square_root), "sqrt")
        )

        when {
            inputText.isBlank() -> edtResult.setText(getString(R.string.default_result))
            expression.checkSyntax() -> edtResult.setText(
                expression.calculate().toBigDecimal(MathContext.DECIMAL64).toPlainString()
            )
            else -> edtResult.setText("")
        }
    }

    companion object {
        fun newInstance() = ResultFragment()
    }

}
