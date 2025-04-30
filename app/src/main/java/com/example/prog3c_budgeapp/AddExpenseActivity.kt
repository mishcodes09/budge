package com.example.prog3c_budgeapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import android.text.InputType
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.prog3c_budgeapp.model.Expense  // adjust the path as needed
import com.example.prog3c_budgeapp.databinding.ActivityAddexpenseBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddexpenseBinding
    private var selectedImageUri: Uri? = null
    private val calendar = Calendar.getInstance()

    private val categories = mutableListOf(
        "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health",
        "Education", "Home", "Travel", "Create New Category"
    )

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            Toast.makeText(this, "Receipt added", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddexpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        updateDateDisplay()
        updateTimeDisplay()

        // Initially hide category icon
        binding.categoryDropdownIcon.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            saveExpense()
        }

        binding.categoryCardView.setOnClickListener {
            showCategoryDialog()
        }

        binding.dateTextView.setOnClickListener {
            showDatePicker()
        }

        binding.timeTextView.setOnClickListener {
            showTimePicker()
        }

        binding.uploadCardView.setOnClickListener {
            showImagePickerOptions()
        }
    }

    private fun showCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Category")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            categories
        )

        builder.setAdapter(adapter) { _, which ->
            if (categories[which] == "Create New Category") {
                showNewCategoryDialog()
            } else {
                binding.categoryText.text = categories[which]
                binding.categoryDropdownIcon.setImageResource(R.drawable.ic_check_white)
                binding.categoryDropdownIcon.visibility = View.VISIBLE
            }
        }

        builder.show()
    }

    private fun showNewCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create New Category")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.hint = "Enter category name"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val newCategory = input.text.toString().trim()
            if (newCategory.isNotEmpty()) {
                categories.add(categories.size - 1, newCategory)
                binding.categoryText.text = newCategory
                binding.categoryDropdownIcon.setImageResource(R.drawable.ic_check_white)
                binding.categoryDropdownIcon.visibility = View.VISIBLE
                Toast.makeText(this, "New category added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateDateDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeDisplay()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.dateTextView.text = dateFormat.format(calendar.time)
    }

    private fun updateTimeDisplay() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.timeTextView.text = timeFormat.format(calendar.time)
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        AlertDialog.Builder(this)
            .setTitle("Add Receipt")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                    2 -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun openCamera() {
        val photoUri = createTempImageUri()
        selectedImageUri = photoUri
        takePicture.launch(photoUri)
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun createTempImageUri(): Uri {
        val tempFileName = "temp_receipt_${System.currentTimeMillis()}"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, tempFileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    private fun saveExpense() {
        val category = binding.categoryText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val amountStr = binding.amountEditText.text.toString()

        if (category == "Create New Category" || category.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val isRecurring = binding.recurringCheckbox.isChecked
        val receiptUri = selectedImageUri?.toString()

        val expense = Expense(
            id = 0,
            category = category,
            description = description,
            amount = amount,
            isRecurring = isRecurring,
            date = calendar.timeInMillis,
            receiptUri = receiptUri
        )

        // TODO: Save to database
        Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show()

        finish()
    }
}
