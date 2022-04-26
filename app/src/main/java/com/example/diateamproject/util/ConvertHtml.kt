package com.example.diateamproject.util

import android.text.Html
import android.text.Spanned

class ConvertHtml {
    fun convertHtmlString(htmlString: String): Spanned {

        val htmlAsString = htmlString
        val htmlAsSpanned: Spanned = Html
            .fromHtml(htmlAsString)
        return htmlAsSpanned

    }
}