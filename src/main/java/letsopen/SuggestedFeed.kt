package letsopen

import JsonCreation
import Log
import ServerValues
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response

class SuggestedFeed(mainURL: String, fullLog: Boolean, protocol: String) : Authentication(mainURL, fullLog, protocol) {

    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun getSuggestedChats(userToken: String, pageSize: Int, pageCursor: String? = null): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_SUGGESTED_CHATS,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_PAGE_SIZE to pageSize,
                ServerValues.FIELD_PAGE_CURSOR to pageCursor
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun getSuggestedChat(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_SUGGESTED_CHAT,
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

    fun followSuggestedChat(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_FOLLOW_SUGGESTED_CHAT,
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

    fun unfollowSuggestedChat(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_UNFOLLOW_SUGGESTED_CHAT,
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

    fun followChatBySendingMessage(userToken: String, text: String, authorName: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_COMMENT_SUGGESTED_CHAT,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_TEXT to text,
                ServerValues.FIELD_AUTHOR_NAME to authorName,
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

    fun getInitialChats(userToken: String, contacts: Array<String>, age: Int, pageSize: Int): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_INITIAL_CHATS,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_CONTACTS to contacts,
                ServerValues.FIELD_AGE to age,
                ServerValues.FIELD_PAGE_SIZE to pageSize
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setSuggestedChatsSeen(userToken: String, chatIds: Array<String?>): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_SUGGESTED_CHATS_SEEN,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_CHAT_IDS to chatIds
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setSuggestedChatViewed(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_SUGGESTED_CHAT_VIEWED,
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