import ru.talenttech.xqa.oknetwork.response.Response

class Log(
    private val baseUrl: String,
    private val adminUrl: String,
    private val urlForGettingToken: String,
    fullLogs: Boolean
) {

    private val fullLog = fullLogs

    fun printExtensiveTechnicalLogs(bodyResponse: Response, text: String) {
        if (fullLog) {
            println("<<<<<<<<<< Response >>>>>>>>>>")
            println("Status code for $text is: ${bodyResponse.code}")
            println("The body we received for $text is: ${bodyResponse.body}")
            println("------------------------------")
        }
    }

    fun printExtensiveApiSentLog(url: String, bodySent: String) {
        if (fullLog) {
            println("<<<<<<<<<< Request >>>>>>>>>>")
            println("The body we sent: $bodySent")

            when (url) {
                baseUrl -> println("To URL: $baseUrl")
                adminUrl -> println("To URL: $adminUrl")
                else -> println("To URL: $urlForGettingToken")
            }
            println("-----------------------------")
        }
    }
}