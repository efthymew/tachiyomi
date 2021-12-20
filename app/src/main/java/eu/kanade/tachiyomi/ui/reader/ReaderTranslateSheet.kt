package eu.kanade.tachiyomi.ui.reader

import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import eu.kanade.tachiyomi.databinding.ReaderTranslateSheetBinding
import eu.kanade.tachiyomi.ui.reader.model.ReaderPage
import eu.kanade.tachiyomi.widget.sheet.BaseBottomSheetDialog
import com.googlecode.tesseract.android.TessBaseAPI


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
        val url = currPage.imageUrl
        val tessBaseApi = TessBaseAPI()
    }
}
