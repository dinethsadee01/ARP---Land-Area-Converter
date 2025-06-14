package com.landconverterapp

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

/**
 * Main Calculator Activity - Handles land area conversions
 * Converts between hectares/acres to acres, roods, and perches
 */
class MainActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var editTextInput: TextInputEditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        editTextInput = findViewById(R.id.editTextInput)
        radioGroupInputType = findViewById(R.id.radioGroupInputType)
        radioHectares = findViewById(R.id.radioHectares)
        radioAcres = findViewById(R.id.radioAcres)
        buttonConvert = findViewById(R.id.buttonConvert)
        cardResults = findViewById(R.id.cardResults)
        textViewInputDisplay = findViewById(R.id.textViewInputDisplay)
        textViewResult = findViewById(R.id.textViewResult)

        // Initially hide results
        cardResults.visibility = View.GONE

        // Set up click listener for convert button
        buttonConvert.setOnClickListener {
            convertLandArea()
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
}
