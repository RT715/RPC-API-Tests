package letsopen

import JsonCreation
import Log
import ServerValues
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response

class Onboarding(mainURL: String, fullLog: Boolean, protocol: String) : Authentication(mainURL, fullLog, protocol) {

    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun setUserAge(token: String, userAge: Int): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_USER_AGE,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_AGE to userAge,
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setUserFcmToken(token: String, fcmToken: String?): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_USER_FCM_TOKEN,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_FCM_TOKEN to fcmToken
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setUserFacebookData(token: String, fbUserId: String? = null, fbAccessToken: String? = null): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_USER_FACEBOOK_DATA,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_FB_USER_ID to fbUserId,
                ServerValues.FIELD_FB_ACCESS_TOKEN to fbAccessToken
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setIp(token: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_IP,
            mapOf(
                ServerValues.FIELD_JWT to token
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun getMyCampaign(token: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_MY_CAMPAIGN,
            mapOf(
                ServerValues.FIELD_JWT to token
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun initFeed(userToken: String, age: Int, contacts: Array<String>): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_INIT_FEED,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_AGE to age,
                ServerValues.FIELD_CONTACTS to contacts
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun getSharingPrompts(userToken: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_SHARED_PROMPTS,
            mapOf(
                ServerValues.FIELD_JWT to userToken
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun createSharedChat(userToken: String, prompt: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_SHARED_CHAT,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_PROMPT to prompt
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