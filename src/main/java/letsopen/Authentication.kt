package letsopen

import Credentials
import JsonCreation
import Log
import ServerValues
import ru.talenttech.xqa.oknetwork.OkNetwork
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response

open class Authentication(domain: String, fullLog: Boolean, protocol: String) {

    var urlForGettingToken = "$protocol://api.letsopen.$domain/debug/get-token"
    var baseUrl = "$protocol://api.letsopen.$domain/json-rpc"
    var adminUrl = "$protocol://api.admin.letsopen.$domain/json-rpc"

    val client = OkNetwork.rpcClient()
    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun getJwtToken(userId: String): Response {

        val body = jsonCreation.getJwtTokenRequestBody(userId)

        logs.printExtensiveApiSentLog(urlForGettingToken, body)

        return client.post(
            urlForGettingToken,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun getUserUIDByPlainPhone(token: String, phoneNumber: String): Response {

        val body = jsonCreation.createBodyString(
                ServerValues.METHOD_GET_UID_BY_PLAIN_PHONE,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_PHONE_NUMBER to phoneNumber
            )
        )

       logs.printExtensiveApiSentLog(adminUrl, body)

        return client.post(
            adminUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun createTestUser(testUserNumber: String): Response {

        val body = jsonCreation.createBodyString(
                ServerValues.METHOD_CREATE_TEST_USER,
            mapOf(
                ServerValues.FIELD_TEST_USER_PHONE_NUMBER to testUserNumber,
                ServerValues.FIELD_API_KEY_FOR_USER_CREATION to Credentials.USER_CREATION_API_KEY
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }
}