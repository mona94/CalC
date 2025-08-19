package com.example.calc

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.RoundingMode

class calculatorActivity : AppCompatActivity() {


    private val TAG: String = "tag"
    private var isOpCalutedOnce: Boolean = true
    private var isJustCalculated: Boolean = false

    private var resultText: String = "0"
    private var firstNumber: String = "0"
    private var displayNumber: String = "0"
    private lateinit var operatorValue: String
    private var isNewCalulation: Boolean = true
    private lateinit var txtDisplay: TextView
    private lateinit var txtResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)

        //code to handle UI

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val height = (screenWidth * 1.3).toInt() // 50% of the screen width

        // Find the view you want to adjust
        val yourView = findViewById<LinearLayout>(R.id.lyt_number)

        // Apply the calculated height dynamically
        val params = yourView.layoutParams
        params.height = height
        yourView.layoutParams = params

        /* declare all the button */
        val btnClear: Button = findViewById<Button>(R.id.btnClear)
        val btnBackSpace: Button = findViewById<Button>(R.id.btnBack)
        val btnEqual: Button = findViewById<Button>(R.id.btnEqual)
        val btnDot: Button = findViewById<Button>(R.id.btnDot)

        val btnPercent: Button = findViewById<Button>(R.id.btnPercent)
        val btnDivide: Button = findViewById<Button>(R.id.btnDivide)
        val btnMutliple: Button = findViewById<Button>(R.id.btnMultiple)
        val btnSub: Button = findViewById<Button>(R.id.btnSub)
        val btnAdd: Button = findViewById<Button>(R.id.btnAdd)

        val btn0: Button = findViewById<Button>(R.id.btn0)
        val btn1: Button = findViewById<Button>(R.id.btn1)
        val btn3: Button = findViewById<Button>(R.id.btn3)
        val btn2: Button = findViewById<Button>(R.id.btn2)
        val btn4: Button = findViewById<Button>(R.id.btn4)
        val btn5: Button = findViewById<Button>(R.id.btn5)
        val btn6: Button = findViewById<Button>(R.id.btn6)
        val btn7: Button = findViewById<Button>(R.id.btn7)
        val btn8: Button = findViewById<Button>(R.id.btn8)
        val btn9: Button = findViewById<Button>(R.id.btn9)

        /* DECLARE  result and display textview*/

        txtResult = findViewById<TextView>(R.id.txt2)
        txtDisplay = findViewById<TextView>(R.id.txt1)

        /*Button click handling*/

//        btn0.setOnClickListener { appendNumber("0") }
//        btn1.setOnClickListener { appendNumber("1") }
//        btn2.setOnClickListener { appendNumber("2") }
//        btn3.setOnClickListener { appendNumber("3") }
//        btn4.setOnClickListener { appendNumber("4") }
//        btn5.setOnClickListener { appendNumber("5") }
//        btn6.setOnClickListener { appendNumber("6") }
//        btn7.setOnClickListener { appendNumber("7") }
//        btn8.setOnClickListener { appendNumber("8") }
//        btn9.setOnClickListener { appendNumber("9") }

        listOf(
            btn0 to "0", btn1 to "1", btn2 to "2", btn3 to "3",
            btn4 to "4", btn5 to "5", btn6 to "6", btn7 to "7",
            btn8 to "8", btn9 to "9"
        ).forEach { (button, number) ->
            button.setOnClickListener { appendNumber(number) }
        }


        btnMutliple.setOnClickListener { setOperator("*") }
        btnAdd.setOnClickListener { setOperator("+") }
        btnSub.setOnClickListener { setOperator("-") }
        btnDivide.setOnClickListener { setOperator("÷") }

        btnEqual.setOnClickListener { handleEqual() }
        btnClear.setOnClickListener { resetAll() }
        btnBackSpace.setOnClickListener { handleBackSpace() }

        btnPercent.setOnClickListener { setOperator("%") }

        btnDot.setOnClickListener {
            if (!txtResult.text.contains(".")) {
                displayNumber = txtResult.text.toString() + "."
                Log.e("TAG", "onCreate: $displayNumber")
                txtResult.text = displayNumber
            }
        }
    }


    private fun handleBackSpace() {
        txtResult.text = txtResult.text.dropLast(1)
        if (txtResult.text.isEmpty()) {
            txtResult.text = "0"
            displayNumber = "0"
        } else {
            displayNumber = txtResult.text.toString()

        }
    }

    private fun resetAll() {
        isOpCalutedOnce = true
        isJustCalculated = false
        resultText = "0"
        firstNumber = "0"
        displayNumber = "0"
        isNewCalulation = true
        operatorValue = ""
        txtDisplay.text = ""
        txtResult.text = "0"
    }

    private fun handleEqual() {

//        displayNumber != "0" &&
//                firstNumber != "0" &&

        if (::operatorValue.isInitialized &&
            operatorValue.isNotBlank()
        ) {
            resultText = calculateResult(
                firstNumber,
                displayNumber,
                operatorValue
            ).toString()

            Log.e(TAG, "handleEqual: $resultText")
            if (resultText.contains("zero")) {
                if (operatorValue.equals("%")) {
                    Log.e(TAG, "inside % ")
                    Toast.makeText(
                        this,
                        "Percentage of zero will always be zero",
                        Toast.LENGTH_SHORT
                    ).show()
                    resultText = "0"
                    isNewCalulation = true
                    isOpCalutedOnce = true
                    txtDisplay.text =
                        checkDouble(firstNumber.toDouble()) + operatorValue + checkDouble(
                            displayNumber.toDouble()
                        ) + "="
                    txtResult.text = checkDouble(resultText.toDouble())
                    firstNumber = resultText
                    isJustCalculated = true
                } else if (operatorValue.contains("÷")) {
                    Log.e(TAG, "inside ÷ ")
                    Toast.makeText(this, "Cann't divide by zero", Toast.LENGTH_SHORT).show()
                    resetAll()
                }
            } else {
                isNewCalulation = true
                isOpCalutedOnce = true
                txtDisplay.text =
                    checkDouble(firstNumber.toDouble()) + operatorValue + checkDouble(displayNumber.toDouble()) + "="
                txtResult.text = checkDouble(resultText.toDouble())
                firstNumber = resultText
                isJustCalculated = true
            }

        } else {
        }

    }

    private fun setOperator(op: String) {
        if (isOpCalutedOnce) {
            if (!isJustCalculated) {
                firstNumber = displayNumber
            }
            operatorValue = op
            isNewCalulation = true
            isOpCalutedOnce = false
            txtDisplay.text = checkDouble(firstNumber.toDouble()) + op
            txtResult.text = "0"
        } else {
            resultText = calculateResult(
                firstNumber,
                displayNumber,
                operatorValue
            ).toString()
            if (resultText.contains("zero")) {
                if (operatorValue.equals("%")) {
                    Log.e(TAG, "inside % ")
                    Toast.makeText(
                        this,
                        "Percentage of zero will always be zero",
                        Toast.LENGTH_SHORT
                    ).show()
                    resultText = "0"
                    operatorValue = op
                    firstNumber = resultText
                    isNewCalulation = true
                    isOpCalutedOnce = false
                    txtDisplay.text = checkDouble(firstNumber.toDouble()) + operatorValue
                    txtResult.text = checkDouble(firstNumber.toDouble())
                } else if (operatorValue.contains("÷")) {
                    Log.e(TAG, "inside ÷ ")
                    Toast.makeText(this, "Cann't divide by zero", Toast.LENGTH_SHORT).show()
                    resetAll()
                }
            } else {
                operatorValue = op
                firstNumber = resultText
                isNewCalulation = true
                isOpCalutedOnce = false
                txtDisplay.text = checkDouble(firstNumber.toDouble()) + operatorValue
                txtResult.text = checkDouble(firstNumber.toDouble())
            }
        }

    }

    private fun checkDouble(numberDisplay: Double): String {
        return if (numberDisplay == numberDisplay.toLong().toDouble()) {
            numberDisplay.toLong().toString() // Remove .0
        } else {
            numberDisplay.toString() // Keep decimal
        }
    }

    private fun appendNumber(numberValue: String) {
        if (isNewCalulation) {
            displayNumber = numberValue
            isNewCalulation = false
        } else {
            displayNumber += numberValue
        }
        txtResult.text = checkDouble(displayNumber.toDouble())
    }

    private fun calculateResult(op1: String, op2: String, operation: String): String {
        val num1 = BigDecimal(op1)
        val num2 = BigDecimal(op2)
        val result = when (operation) {
            "+" -> num1.add(num2)
            "-" -> num1.subtract(num2)
            "*" -> num1.multiply(num2)
            "÷" -> {
                if (num2.compareTo(BigDecimal.ZERO) == 0) {
                    return "zero"
                } else {
                    num1.divide(num2, 10, RoundingMode.HALF_UP) // specify scale and rounding
                }
            }

            "%" -> {
                if (num1.compareTo(BigDecimal.ZERO) == 0) {
                    return "zero"
                } else {
                    num1.multiply(num2).divide(BigDecimal(100))
                }
            }

            else -> BigDecimal.ZERO
        }

        return result.stripTrailingZeros().toPlainString()
    }

}