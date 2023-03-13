package letsopen

import JsonCreation
import Log
import ServerValues
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response

class Home(mainURL: String, fullLog: Boolean, protocol: String) : Authentication(mainURL, fullLog, protocol) {

    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun archiveFeedItem(token: String, feedItemId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_ARCHIVE_FEED_ITEM,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_FEED_ITEM_ID to feedItemId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun unarchiveFeedItem(token: String, feedItemId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_UNARCHIVE_FEED_ITEM,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_FEED_ITEM_ID to feedItemId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun muteFeedItem(token: String, feedItemId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_MUTE_FEED_ITEM,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_FEED_ITEM_ID to feedItemId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun unmuteFeedItem(token: String, feedItemId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_UNMUTE_FEED_ITEM,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_FEED_ITEM_ID to feedItemId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun clearUnreadMessageCount(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CLEAR_UNREAD_MESSAGE_COUNT,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_CHAT_ID to chatId
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