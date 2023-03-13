package letsopen

import Credentials
import JsonCreation
import Log
import ServerValues
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response

class Documents(mainURL: String, fullLog: Boolean, protocol: String) : Authentication(mainURL, fullLog, protocol) {

    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun getDocument(collectionName: String, documentId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_DOCUMENT,
            mapOf(
                ServerValues.FIELD_COLLECTION_NAME to collectionName,
                ServerValues.FIELD_DOCUMENT_ID to documentId,
                ServerValues.FIELD_API_KEY_FOR_GETTING_DOCUMENT to Credentials.GET_DOCUMENT_API_KEY
            )
        )

        logs.printExtensiveApiSentLog(adminUrl, body)

        return client.post(
            adminUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }
}