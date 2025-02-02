package com.example.graphcalculator

import android.os.Bundle

import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var inputExpression: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputExpression = findViewById(R.id.inputExpression)

        setupButtons()
    }

    private fun setupButtons() {
        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnDot, R.id.btnOpenParen, R.id.btnCloseParen
        )

        buttonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener { appendToExpression((it as Button).text.toString()) }
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener { removeLastChar() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { inputExpression.setText("") }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateExpression() }
    }

    private fun appendToExpression(value: String) {
        inputExpression.setText(inputExpression.text.toString() + value)
    }

    private fun removeLastChar() {
        val text = inputExpression.text.toString()
        if (text.isNotEmpty()) {
            inputExpression.setText(text.substring(0, text.length - 1))
        }
    }

    private fun calculateExpression() {
        val expressionText = inputExpression.text.toString()

        if (expressionText.isEmpty()) {
            showResultDialog("Ошибка", "Введите выражение!")
            return
        }

        try {
            val expression = ExpressionBuilder(expressionText).build()
            val result = expression.evaluate()

            if (result.isInfinite() || result.isNaN()) {
                showResultDialog("Ошибка", "Некорректное выражение")
            } else {
                showResultDialog("Результат", result.toString())
            }
        } catch (e: Exception) {
            showResultDialog("Ошибка", "Некорректное выражение")
        }
    }

    private fun showResultDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}