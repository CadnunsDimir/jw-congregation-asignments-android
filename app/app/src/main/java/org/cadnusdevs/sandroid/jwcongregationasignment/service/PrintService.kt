package org.cadnusdevs.sandroid.jwcongregationasignment.service

import android.content.ContentValues.TAG
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.print.PrintJob
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils.*
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend

class PrintService(private val activity: Context) {
    private val printJobs = ArrayList<PrintJob>()
    private var mWebView: WebView? = null

    fun print(
        title: String,
        meetings: List<MeetingDay>,
        weekends: List<Weekend>,
        invitedSpeechesCongregation: String
    ) {
        val webView = WebView(activity)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "page finished loading $url")
                createWebPrintJob(title, view)
                mWebView = null
            }
        }
        val css = "<style>${ activity.resources.getString(R.string.print_css) }</style>"

        val midWeekMeetingsTable = meetingTable(meetings, WeekDay.midWeekDays)
        val weekendMeetingsTable = meetingTable(meetings, WeekDay.weekEndDays)
        val weekendPresidentReaderTable = presidentReaderTable(weekends)
        val speechesArrangement = speechesArrangementTable(invitedSpeechesCongregation, weekends)

        // Generate an HTML document on the fly:
        val htmlDocument =
            "<html><head>$css</head><body><h1>$title</h1>$midWeekMeetingsTable $weekendMeetingsTable $weekendPresidentReaderTable $speechesArrangement</body></html>"
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
    }

    private fun speechesArrangementTable(
        invitedSpeechesCongregation: String,
        weekends: List<Weekend>
    ): String {
        val table = StringBuilder()
        table.append("<h2>Arreglo de Discurso</h2>")
        table.append("<table class='speeches-arrangement'>")
        table.append("<tr><th colspan='3'>Estaremos en Arreglo con la Congregación $invitedSpeechesCongregation</th></tr>")
        table.append("<tr><th>Fecha</th><th>Presidencia</th><th>Lector Atalaya</th></tr>")
        weekends.forEach {
            table.append("<tr>${td(it.date.shortDate)}${td(it.speech.title)}${td(it.speech.speaker)}</tr>")
        }
        table.append("</table>")
        return table.toString()
    }

    private fun presidentReaderTable(weekends: List<Weekend>): Any {
        val table = StringBuilder()
        table.append("<h2>Reunión de Fin de Semana</h2>")
        table.append("<table class='president-reader'>")
        table.append("<tr><th>Fecha</th><th>Presidencia</th><th>Lector Atalaya</th></tr>")
        weekends.forEach {
            table.append("<tr>${td(it.date.shortDate)}${td(it.meetingPresident)}${td(it.watchTowerReader)}</tr>")
        }
        table.append("</table>")
        return table.toString()
    }

    private fun meetingTable(meetings: List<MeetingDay>, days: Array<WeekDay>) = generateHtmlTable(meetings.filter { meetingDay -> days.contains(meetingDay.date.weekDay) })

    private fun generateHtmlTable(meetings: List<MeetingDay>): String {
        val table = StringBuilder()
        val weekDay = if(meetings.isNotEmpty()) meetings[0].date.dayOfWeekAsString(SupportedLanguages.Es) else "Fecha"
        table.append("<table class='full-width'>")
        table.append("<tr><th class='grey'>$weekDay</th><th colspan='2'>Acomodadores</th><th colspan='2'>Microfonos</th><th>Computadora</th><th>Sonido</th><th>Limpieza</th></tr>")
        meetings.forEach {
            table.append("<tr>${td(it.date.shortDate)} ${td(it.usherA)}${td(it.usherB)}${td(it.microphoneA)}${td(it.microphoneB)}${td(it.computer)}${td(it.soundSystem)}${td(it.cleanGroupId)}</tr>")
        }
        table.append("</table>")
        return table.toString()
    }

    private fun td(value: Any) = "<td>${value}</td>"
    private fun td(brother: Brother?): String {
        val text = if(brother == null || brother.id == 0L) "" else brother.name
        return td(text)
    }

    private fun createWebPrintJob(documentTitle: String, webView: WebView) {

        // Get a PrintManager instance
        (activity?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "$documentTitle"
            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build()
            ).also { printJob ->
                // Save the job object for later status checking
                printJobs.add(printJob)
            }
        }
    }
}