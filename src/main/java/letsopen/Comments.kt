package letsopen

import JsonCreation
import Log
import ServerValues
import enums.MessageTypes
import org.json.JSONArray
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response

class Comments(domain: String, fullLog: Boolean, protocol: String) : Authentication(domain, fullLog, protocol) {

    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun createComment(userToken: String, authorName: String, text: String, postId: String?): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_COMMENT,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_AUTHOR_NAME to authorName,
                ServerValues.FIELD_TEXT to text,
                ServerValues.FIELD_CHAT_ID to postId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun createDM(
        userToken: String,
        authorName: String,
        peerName: String,
        text: String,
        postId: String? = null
    ): Response {

        val dmPostID = "${postId}_${authorName}_${peerName}"

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_DIRECT_MESSAGE,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_TEXT to text,
                ServerValues.FIELD_AUTHOR_NAME to authorName,
                ServerValues.FIELD_PEER_NAME to peerName,
                ServerValues.FIELD_PARENT_CHAT_ID to postId,
                ServerValues.FIELD_CHAT_ID to dmPostID
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }


    fun blockUser(token: String, postId: String?, authorName: String, shortText: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_BLOCK_USER,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_CHAT_ID to postId,
                ServerValues.FIELD_AUTHOR_NAME to authorName,
                ServerValues.FIELD_SHORT_TEXT to shortText
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun unblockUser(userToken: String, chatId: String, authorName: String): Response {

        val body = jsonCreation.createBodyString(
             ServerValues.METHOD_UNBLOCK_USER,
             mapOf(
                 ServerValues.FIELD_JWT to userToken,
                 ServerValues.FIELD_CHAT_ID to chatId,
                 ServerValues.FIELD_AUTHOR_NAME to authorName
             )
         )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun createComplaint(token: String, cause: String, itemId: String, itemType: MessageTypes, text: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_COMPLAINT,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_CAUSE to cause,
                ServerValues.FIELD_ITEM_ID to itemId,
                ServerValues.FIELD_ITEM_TYPE to getServerMessageType(itemType),
                ServerValues.FIELD_TEXT to text
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun acknowledgeCommentsRejection(supervisorToken: String, commentID: String): Response {

        val comments = JSONArray()
        comments.put(commentID)

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_ACKNOWLEDGE_COMMENT_REJECTION,
            mapOf(
                ServerValues.FIELD_JWT to supervisorToken,
                ServerValues.FIELD_COMMENT_IDS to comments,
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun acknowledgeDMRejection(supervisorToken: String, directMessageID: String): Response {

        val directMessageIds = JSONArray()
        directMessageIds.put(directMessageID)

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_ACKNOWLEDGE_DM_REJECTION,
            mapOf(
                ServerValues.FIELD_JWT to supervisorToken,
                ServerValues.FIELD_DIRECT_MESSAGE_IDS to directMessageIds,
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }


    fun deleteComment(userToken: String, commentId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_DELETE_COMMENT,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_COMMENT_ID to commentId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setTitle(userToken: String, chatId: String, text: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_CHAT_TITLE,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_CHAT_ID to chatId,
                ServerValues.FIELD_TEXT to text
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun deleteDM(userToken: String, messageId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_DELETE_DIRECT_MESSAGE,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_MESSAGE_ID to messageId
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun getChatParticipants(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_CHAT_PARTICIPANTS,
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

    fun createAnonComment(chatId: String, text: String, anonToken: String?): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_ANON_COMMENT,
            mapOf(
                ServerValues.FIELD_CHAT_ID to chatId,
                ServerValues.FIELD_TEXT to text,
                ServerValues.FIELD_ANON_TOKEN to anonToken
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    private fun getServerMessageType(messageType: MessageTypes) = when (messageType) {
        MessageTypes.COMMENT -> ServerValues.MESSAGE_TYPE_COMMENT
        MessageTypes.DIRECT_MESSAGE -> ServerValues.MESSAGE_TYPE_DIRECT_MESSAGE
    }
}