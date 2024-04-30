package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var imageUri: Uri // Definisikan di tingkat kelas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString) // Inisialisasi di onCreate
            displayImage(imageUri)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(errorMessage: String) {
                        Log.d(TAG, "Error: $errorMessage")
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let { showResults(it) }
                    }
                }
            )
            imageClassifierHelper.classifyImage(imageUri)
        } else {
            Log.e(TAG, "No image URI provided")
            finish()
        }
    }

    private fun showResults(results: List<Classifications>) {
        val topResult = results[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }
        binding.resultText.text = "$label ${score.formatToString()}"

        val resultLabel = "$label ${score.formatToString()}"
        val imagePath = imageUri.toString() // Gunakan imageUri yang didefinisikan di tingkat kelas
        val historyEntity = HistoryEntity(imagePath = imagePath, result = resultLabel)

        binding.saveButton.setOnClickListener {
            saveToDatabase()
        }
    }

    private fun displayImage(uri: Uri) {
        Log.d(TAG, "Display Image: $uri")
        binding.resultImage.setImageURI(uri)
    }

    private fun saveToDatabase() {
        val resultLabel = binding.resultText.text.toString()
        val imagePath = imageUri.toString()

        val historyEntity = HistoryEntity(imagePath = imagePath, result = resultLabel)
        val historyRepository = HistoryRepository(application)
        historyRepository.insert(historyEntity)

        showToast("Data saved to Room Database")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val TAG = "imagepPicker"
    }
}
