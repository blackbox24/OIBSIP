package com.example.android_task1_unitconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    // Unit lists for each category
    String[] lengthUnits = {"Centimeter", "Meter", "Kilometer", "Inch", "Foot"};
    String[] weightUnits = {"Gram", "Kilogram", "Pound", "Ounce"};
    String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    // Declare the widgets
    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button btnConvert;
    TextView textViewResult;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the widgets to the XML
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextValue = findViewById(R.id.editTextValue);
        btnConvert = findViewById(R.id.btnConvert);
        textViewResult = findViewById(R.id.textViewResult);

        // Create the list of categories
        String[] categories = {"Length", "Weight", "Temperature"};

        // Create an adapter (this is what puts the list into the Spinner)
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );

        spinnerCategory.setAdapter(categoryAdapter);

        // Attach the adapter to the Category Spinner
        // When category changes, update the unit spinners
        spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCategory = categories[position];
                updateUnitSpinners(selectedCategory);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set default units when app starts
        updateUnitSpinners("Length");

        // Convert button click
        btnConvert.setOnClickListener(v -> {
            // 1. Get the value the user typed
            String inputText = editTextValue.getText().toString().trim();

            // 2. Check if the input is empty
            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Try to convert the text to a number
            double value;
            try {
                value = Double.parseDouble(inputText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }
            // FIX 2: Check if spinners actually have a selection to prevent crashes
            if (spinnerFrom.getSelectedItem() == null || spinnerTo.getSelectedItem() == null) {
                Toast.makeText(this, "Please select your units", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Get selected units
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            // 5. Do the conversion
            double result = convertValue(value, fromUnit, toUnit, category);

            // 6. Show the result
            textViewResult.setText(String.format("%.4f %s", result, toUnit));
        });
    }
    private void updateUnitSpinners(String category) {
        String[] units;

        if (category.equals("Length")) {
            units = lengthUnits;
        } else if (category.equals("Weight")) {
            units = weightUnits;
        } else {
            units = temperatureUnits;
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                units
        );

        spinnerFrom.setAdapter(unitAdapter);
        spinnerTo.setAdapter(unitAdapter);

        spinnerFrom.setSelection(0);
        spinnerTo.setSelection(0);
    }
    private double convertValue(double value, String fromUnit, String toUnit, String category) {

        // If both units are the same, just return the same value
        if (fromUnit.equals(toUnit)) {
            return value;
        }

        // ========== LENGTH ==========
        switch (category) {
            case "Length":
                // First convert everything to meters
                double inMeters = 0;

                switch (fromUnit) {
                    case "Centimeter":
                        inMeters = value / 100;
                        break;
                    case "Meter":
                        inMeters = value;
                        break;
                    case "Kilometer":
                        inMeters = value * 1000;
                        break;
                    case "Inch":
                        inMeters = value * 0.0254;
                        break;
                    case "Foot":
                        inMeters = value * 0.3048;
                        break;
                }

                // Then convert from meters to the target unit
                switch (toUnit) {
                    case "Centimeter":
                        return inMeters * 100;
                    case "Meter":
                        return inMeters;
                    case "Kilometer":
                        return inMeters / 1000;
                    case "Inch":
                        return inMeters / 0.0254;
                    case "Foot":
                        return inMeters / 0.3048;
                }
                break;

            // ========== WEIGHT ==========
            case "Weight":
                // First convert everything to grams
                double inGrams = 0;

                switch (fromUnit) {
                    case "Gram":
                        inGrams = value;
                        break;
                    case "Kilogram":
                        inGrams = value * 1000;
                        break;
                    case "Pound":
                        inGrams = value * 453.592;
                        break;
                    case "Ounce":
                        inGrams = value * 28.3495;
                        break;
                }

                // Then convert from grams to the target unit
                switch (toUnit) {
                    case "Gram":
                        return inGrams;
                    case "Kilogram":
                        return inGrams / 1000;
                    case "Pound":
                        return inGrams / 453.592;
                    case "Ounce":
                        return inGrams / 28.3495;
                }
                break;

            // ========== TEMPERATURE ==========
            case "Temperature":
                // First convert everything to Celsius
                double inCelsius = 0;

                switch (fromUnit) {
                    case "Celsius":
                        inCelsius = value;
                        break;
                    case "Fahrenheit":
                        inCelsius = (value - 32) * 5 / 9;
                        break;
                    case "Kelvin":
                        inCelsius = value - 273.15;
                        break;
                }

                // Then convert from Celsius to the target unit
                switch (toUnit) {
                    case "Celsius":
                        return inCelsius;
                    case "Fahrenheit":
                        return (inCelsius * 9 / 5) + 32;
                    case "Kelvin":
                        return inCelsius + 273.15;
                }
                break;
        }

        // If something goes wrong, return 0
        return 0;
    }
}