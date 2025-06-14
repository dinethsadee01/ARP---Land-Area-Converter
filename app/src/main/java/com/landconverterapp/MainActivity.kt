package com.landconverterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat

/**
 * Main Calculator Activity - Handles land area conversions
 * Converts between hectares/acres to acres, roods, and perches
 */
class MainActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var editTextInput: TextInputEditText
    private lateinit var inputLayout: TextInputLayout
    private lateinit var radioGroupInputType: RadioGroup
    private lateinit var radioHectares: RadioButton
    private lateinit var radioAcres: RadioButton
    private lateinit var buttonConvert: MaterialButton
    private lateinit var cardResults: CardView
    private lateinit var textViewInputDisplay: TextView
    private lateinit var textViewResult: TextView

    // Constants for conversion
    private val HECTARES_TO_ACRES = 2.47105
    private val ACRES_TO_ROODS = 4.0
    private val ROODS_TO_PERCHES = 40.0

    // Constants for input validation
    private val MAX_DIGITS_BEFORE_DECIMAL = 9
    private val MAX_DIGITS_AFTER_DECIMAL = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        editTextInput = findViewById(R.id.editTextInput)
        inputLayout = findViewById(R.id.inputLayout)
        radioGroupInputType = findViewById(R.id.radioGroupInputType)
        radioHectares = findViewById(R.id.radioHectares)
        radioAcres = findViewById(R.id.radioAcres)
        buttonConvert = findViewById(R.id.buttonConvert)
        cardResults = findViewById(R.id.cardResults)
        textViewInputDisplay = findViewById(R.id.textViewInputDisplay)
        textViewResult = findViewById(R.id.textViewResult)

        // Initially hide results
        cardResults.visibility = View.GONE

        // Add input validation with TextWatcher
        editTextInput.addTextChangedListener(object : TextWatcher {
            // Store the previous valid input to restore if needed
            private var previousValidInput = ""
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null) return

                val input = s.toString()

                // Allow empty string
                if (input.isEmpty()) {
                    previousValidInput = input
                    return
                }

                // Check if it's a valid number format
                if (input.matches(Regex("^\\d{0,9}(\\.\\d{0,9})?$"))) {
                    // Valid input - store it
                    previousValidInput = input
                } else {
                    // Invalid input - restore previous valid input
                    isEditing = true
                    s.replace(0, s.length, previousValidInput)
                    isEditing = false

                    // Show a message to the user
                    if (input.contains(".")) {
                        // Check if the issue is with digits after decimal
                        val parts = input.split(".")
                        if (parts.size > 1 && parts[1].length > MAX_DIGITS_AFTER_DECIMAL) {
                            Toast.makeText(this@MainActivity,
                                "Maximum $MAX_DIGITS_AFTER_DECIMAL decimal places allowed",
                                Toast.LENGTH_SHORT).show()
                        }
                    } else if (input.length > MAX_DIGITS_BEFORE_DECIMAL) {
                        // Issue with digits before decimal
                        Toast.makeText(this@MainActivity,
                            "Maximum $MAX_DIGITS_BEFORE_DECIMAL digits allowed",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                // Hide results when input changes
                cardResults.visibility = View.GONE
            }
        })

        // Set up click listener for convert button
        buttonConvert.setOnClickListener {
            convertLandArea()
        }

        // Add listener for radio button changes to reset results
        radioGroupInputType.setOnCheckedChangeListener { _, _ ->
            // Hide results when input type changes
            cardResults.visibility = View.GONE
        }
        
        // Add listener for clear button to reset results
        inputLayout.setEndIconOnClickListener {
            // Clear the input field (default behavior)
            editTextInput.text?.clear()
            
            // Hide results when input is cleared
            cardResults.visibility = View.GONE
        }
    }

    /**
     * Converts the input value based on selected input type
     */
    private fun convertLandArea() {
        val inputText = editTextInput.text.toString()

        if (inputText.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
            return
        }

        val inputValue = inputText.toDoubleOrNull()
        if (inputValue == null) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            return
        }

        // Perform conversion based on input type
        val isHectaresSelected = radioHectares.isChecked
        val result = if (isHectaresSelected) {
            convertHectaresToAcresRoodsPerches(inputValue)
        } else {
            convertAcresToAcresRoodsPerches(inputValue)
        }

        // Display results
        val inputType = if (isHectaresSelected) "hectares" else "acres"
        textViewInputDisplay.text = "$inputValue $inputType equals:"
        textViewResult.text = result
        cardResults.visibility = View.VISIBLE
    }

    /**
     * Converts hectares to acres, roods, and perches format
     */
    private fun convertHectaresToAcresRoodsPerches(hectares: Double): String {
        return convertAcresToAcresRoodsPerches(hectares * HECTARES_TO_ACRES)
    }

    /**
     * Converts acres to acres, roods, and perches format
     * Returns a formatted string like: "5 acres, 2 roods, 10 perches"
     */
    private fun convertAcresToAcresRoodsPerches(acres: Double): String {
        val wholeAcres = acres.toInt()
        val remainingAcres = acres - wholeAcres

        val roods = remainingAcres * ACRES_TO_ROODS
        val wholeRoods = roods.toInt()
        val remainingRoods = roods - wholeRoods

        val perches = remainingRoods * ROODS_TO_PERCHES
        val wholePerches = perches.toInt()

        // Format decimal places for perches
        val df = DecimalFormat("#")
        val formattedPerches = df.format(wholePerches)

        return "$wholeAcres acres, $wholeRoods roods, $formattedPerches perches"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Show confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Exit Application")
            .setMessage("Do you want to exit the application?")
            .setPositiveButton("Yes") { _, _ ->
                // If user clicks "Yes", proceed with normal back button behavior
                super.onBackPressed()
            }
            .setNegativeButton("No", null) // If user clicks "No", dismiss dialog and continue
            .show()
    }
}