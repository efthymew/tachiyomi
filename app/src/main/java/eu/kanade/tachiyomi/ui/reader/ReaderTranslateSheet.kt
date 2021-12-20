package eu.kanade.tachiyomi.ui.reader

import android.view.LayoutInflater
import android.view.View
import eu.kanade.tachiyomi.databinding.ReaderTranslateSheetBinding
import eu.kanade.tachiyomi.ui.reader.model.ReaderPage
import eu.kanade.tachiyomi.widget.sheet.BaseBottomSheetDialog

class ReaderTranslateSheet(
    private val activity: ReaderActivity,
    private val page: ReaderPage?
) : BaseBottomSheetDialog(activity) {

    private lateinit var binding: ReaderTranslateSheetBinding
    private val apiKey: String = "d05a440ff188957"

    override fun createView(inflater: LayoutInflater): View {
        binding = ReaderTranslateSheetBinding.inflate(activity.layoutInflater, null, false)
        return binding.root
    }

    fun translate() {
        val currPage = page ?: return
        val translation = activity.translatePage(currPage) ?: null
        binding.translation.setText("translation changed here!")
        show()
    }
}
