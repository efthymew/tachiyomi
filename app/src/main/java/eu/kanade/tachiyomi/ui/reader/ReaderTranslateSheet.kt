package eu.kanade.tachiyomi.ui.reader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import com.googlecode.tesseract.android.TessBaseAPI
import eu.kanade.tachiyomi.databinding.ReaderTranslateSheetBinding
import eu.kanade.tachiyomi.ui.reader.model.ReaderPage
import eu.kanade.tachiyomi.widget.sheet.BaseBottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception

class ReaderTranslateSheet(
    private val activity: ReaderActivity,
    private val page: ReaderPage?
) : BaseBottomSheetDialog(activity) {

    private lateinit var binding: ReaderTranslateSheetBinding
    private val DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Tachiyomi/"
    private val TESSDATA = "tessdata"

    override fun createView(inflater: LayoutInflater): View {
        binding = ReaderTranslateSheetBinding.inflate(activity.layoutInflater, null, false)
        return binding.root
    }

    fun translate() {
        val currPage = page ?: return
        val streamFn = currPage.stream ?: return
        val fileStream: InputStream = streamFn()
        prepareOCR()
        val tessBaseApi = TessBaseAPI()
        val text = getText(fileStream, tessBaseApi)
        binding.translation.setText(text)
    }

    private fun getText(fileStream: InputStream, api: TessBaseAPI): String? {
        val bitmap: Bitmap = BitmapFactory.decodeStream(fileStream)
        api.init(DATA_PATH, "jpn")
        api.setImage(bitmap)
        var text = ""
        try {
            // call api
        } catch (e: Exception) {
        }
        return text
    }

    private fun prepareDir(path: String) {
        val dir = File(path)
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                // nothing
            }
        }
    }

    private fun prepareOCR() {
        try {
            prepareDir(DATA_PATH + TESSDATA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        copyOCRModel(TESSDATA)
    }

    private fun copyOCRModel(path: String) {
        try {
            val files = activity.assets.list(path) ?: return
            for (file in files) {
                val dataPath = "$DATA_PATH$path/$file"
                if (!File(dataPath).exists()) {
                    val inp: InputStream = activity.assets.open("$path/$file")
                    val out: OutputStream = FileOutputStream(dataPath)
                    val buf = ByteArray(1024)
                    var len = inp.read(buf)
                    while (len > 0) {
                        out.write(buf, 0, len)
                        len = inp.read(buf)
                    }
                    inp.close()
                    out.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
