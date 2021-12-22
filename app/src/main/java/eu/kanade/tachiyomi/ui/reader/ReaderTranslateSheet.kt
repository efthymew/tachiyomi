package eu.kanade.tachiyomi.ui.reader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import eu.kanade.tachiyomi.databinding.ReaderTranslateSheetBinding
import eu.kanade.tachiyomi.ui.reader.model.ReaderPage
import eu.kanade.tachiyomi.widget.sheet.BaseBottomSheetDialog

class ReaderTranslateSheet(
    private val activity: ReaderActivity,
    private val page: ReaderPage?
) : BaseBottomSheetDialog(activity) {

    private lateinit var binding: ReaderTranslateSheetBinding

    override fun createView(inflater: LayoutInflater): View {
        binding = ReaderTranslateSheetBinding.inflate(activity.layoutInflater, null, false)
        return binding.root
    }

    fun translate() {
        val currPage = page ?: return
        val streamFn = currPage.stream ?: return
        val bitmap: Bitmap = BitmapFactory.decodeStream(streamFn())

        // mlkit
        val text = mlkitGetText(bitmap)
    }

    /**
     * Manages changing the view based off mlkit processing of the
     * text
     */
    private fun mlkitGetText(bm: Bitmap) {
        val recognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
        val image = InputImage.fromBitmap(bm, 0)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // text detected in image

                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.JAPANESE)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
                val jpTranslator = Translation.getClient(options)
                var conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()

                // download language models
                jpTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        // Model downloaded successfully. Okay to start translating.

                        // iterate over text blocks and translate

                        jpTranslator.translate(visionText.text)
                            .addOnSuccessListener { translatedText ->
                                // Translation successful.
                                binding.translation.setText(visionText.text + "\n" + translatedText)
                            }
                            .addOnFailureListener {
                                // Error.
                                binding.translation.setText("Internal Error!")
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Model couldn’t be downloaded or other internal error.
                        binding.translation.setText("Internal Error!")
                    }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }
}
