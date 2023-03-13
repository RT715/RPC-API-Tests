package letsopen

import CommonUtils
import Credentials
import JsonCreation
import Log
import ServerValues
import enums.MessageTypes
import enums.ModerationDecisionType
import enums.ModerationRejectionSeverity
import enums.PostTag
import org.json.JSONArray
import ru.talenttech.xqa.oknetwork.request.ContentType
import ru.talenttech.xqa.oknetwork.response.Response
import java.rmi.ServerError

class ExternalIntegrations(mainURL: String, fullLog: Boolean, protocol: String) : Authentication(mainURL, fullLog, protocol) {

    private val jsonCreation = JsonCreation()
    private val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    fun createSmsInvites(token: String): Response {

        val userMeta = JSONArray()
        userMeta.put(
            mapOf(
                "to" to Credentials.FIRST_TEST_USER_PHONE_NUMBER,
                "firstName" to Credentials.BOT_NAME
            )
        )

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_SMS_INVITES,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_INVITES to userMeta
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setContacts(token: String): Response {

        val phone1 = CommonUtils().randomPhoneGenerator()
        val phone2 = CommonUtils().randomPhoneGenerator()

        val phones = JSONArray()
        phones.put(phone1)
        phones.put(phone2)

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_CONTACTS,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_CONTACTS to phones
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun palUpUsers(token: String, usersPhoneNumbers: Array<String>): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_CONTACTS,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_CONTACTS to usersPhoneNumbers
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setContactsForADS(token: String): Response {

        val phone1 = CommonUtils().randomPhoneGenerator()
        val phone2 = CommonUtils().randomPhoneGenerator()

        val phones = JSONArray()
        phones.put(phone1)
        phones.put(phone2)

        val mail1 = CommonUtils().randomEmailGenerator()
        val mail2 = CommonUtils().randomEmailGenerator()

        val mails = JSONArray()
        mails.put(mail1)
        mails.put(mail2)

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_CONTACTS_FOR_ADS,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_PHONE_NUMBERS to phones,
                ServerValues.FIELD_EMAILS to mails
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun createAppReview(token: String, text: String, score: Int): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_CREATE_APP_REVIEW,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_TEXT to text,
                ServerValues.FIELD_SCORE to score
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun approveContent(
        token: String,
        contentId: String,
        contentType: MessageTypes,
        decisionType: ModerationDecisionType,
        notes: String
    ) = moderateContent(
        token,
        contentId,
        contentType,
        decisionType,
        notes
    )

    fun rejectContent(
        token: String,
        contentId: String,
        contentType: MessageTypes,
        decisionType: ModerationDecisionType,
        notes: String,
        rejectionReason: String,
        rejectionSeverity: ModerationRejectionSeverity
    ) = moderateContent(
        token,
        contentId,
        contentType,
        decisionType,
        notes,
        rejectionReason,
        rejectionSeverity
    )

    private fun moderateContent(
        token: String,
        contentId: String,
        contentType: MessageTypes,
        decisionType: ModerationDecisionType,
        notes: String,
        rejectionReason: String? = null,
        rejectionSeverity: ModerationRejectionSeverity? = null
    ): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_MODERATE_CONTENT,
            mapOf(ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_CONTENT_ID to contentId,
                ServerValues.FIELD_CONTENT_TYPE to getServerContentTypeValue(contentType),
                ServerValues.FIELD_DECISION to getServerDecisionTypeValue(decisionType),
                ServerValues.FIELD_NOTES to notes,
                ServerValues.FIELD_REJECTION_REASON to rejectionReason,
                ServerValues.FIELD_REJECTION_SEVERITY to rejectionSeverity?.let { getServerRejectionSeverityValue(it) }
            )
        )

        logs.printExtensiveApiSentLog(adminUrl, body)

        return client.post(
            adminUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun approveTitle(
        token: String,
        chatId: String,
        contentId: String,
        decisionType: ModerationDecisionType,
        notes: String
    ) = moderateTitle(
        token,
        chatId,
        contentId,
        decisionType,
        notes
    )

    private fun moderateTitle(
        token: String,
        chatId: String,
        contentId: String,
        decisionType: ModerationDecisionType,
        notes: String,
        rejectionReason: String? = null,
        rejectionSeverity: ModerationRejectionSeverity? = null
    ): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_MODERATE_TITLE,
            mapOf(ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_CHAT_ID to chatId,
                ServerValues.FIELD_CONTENT_ID to contentId,
                ServerValues.FIELD_DECISION to getServerDecisionTypeValue(decisionType),
                ServerValues.FIELD_NOTES to notes,
                ServerValues.FIELD_REJECTION_REASON to rejectionReason,
                ServerValues.FIELD_REJECTION_SEVERITY to rejectionSeverity?.let { getServerRejectionSeverityValue(it) }
            )
        )

        logs.printExtensiveApiSentLog(adminUrl, body)

        return client.post(
            adminUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setPostTag(token: String, postId: String, tag: PostTag): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_POST_TAG,
            mapOf(
                ServerValues.FIELD_JWT to token,
                ServerValues.FIELD_POST_ID to postId,
                ServerValues.FIELD_TAG to getServerPostTagValue(tag)
            )
        )

        logs.printExtensiveApiSentLog(adminUrl, body)

        return client.post(
            adminUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun setUserLastSession(userToken: String, time: Long): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_SET_USER_LAST_SESSION,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_LAST_SESSION_AT_UTC to time,
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun getRatingPromptness(userToken: String, algo: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_RATING_PROMPTNESS,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
                ServerValues.FIELD_ALGO to algo
            )
        )

        logs.printExtensiveApiSentLog(baseUrl, body)

        return client.post(
            baseUrl,
            contentType = ContentType.JSON,
            body = body
        )
    }

    fun deleteUser(userToken: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_DELETE_USER,
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

    fun getSharedChatFeed(userToken: String, chatId: String): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_SHARED_CHAT_FEED,
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

    fun getSharedChatInfo(chatId: String, anonToken: String?): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_GET_SHARED_CHAT_INFO,
            mapOf(
                ServerValues.FIELD_CHAT_ID to chatId,
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

    fun publishAnonComments(userToken: String, anonToken: String?): Response {

        val body = jsonCreation.createBodyString(
            ServerValues.METHOD_PUBLISH_ANON_COMMENTS,
            mapOf(
                ServerValues.FIELD_JWT to userToken,
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

    private fun getServerContentTypeValue(contentType: MessageTypes) = when (contentType) {
        MessageTypes.COMMENT -> ServerValues.MESSAGE_TYPE_COMMENT
        MessageTypes.DIRECT_MESSAGE -> ServerValues.MESSAGE_TYPE_DIRECT_MESSAGE
    }

    private fun getServerDecisionTypeValue(decision: ModerationDecisionType) = when (decision) {
        ModerationDecisionType.APPROVED -> ServerValues.MODERATION_DECISION_TYPE_APPROVED
        ModerationDecisionType.REJECTED -> ServerValues.MODERATION_DECISION_TYPE_REJECTED
    }

    private fun getServerRejectionSeverityValue(severity: ModerationRejectionSeverity) = when (severity) {
        ModerationRejectionSeverity.LOW -> ServerValues.MODERATION_REJECTION_SEVERITY_LOW
        ModerationRejectionSeverity.MIDDLE -> ServerValues.MODERATION_REJECTION_SEVERITY_MIDDLE
        ModerationRejectionSeverity.HIGH -> ServerValues.MODERATION_REJECTION_SEVERITY_HIGH
    }

    private fun getServerPostTagValue(postTag: PostTag) = when (postTag) {
        PostTag.SELF_DISCLOSURE -> ServerValues.POST_TAG_SELF_DISCLOSURE
        PostTag.QUESTION_PROMPT -> ServerValues.POST_TAG_QUESTION_PROMPT
        PostTag.OTHER_HIGH -> ServerValues.POST_TAG_OTHER_HIGH
        PostTag.GREETING -> ServerValues.POST_TAG_GREETING
        PostTag.APP_QUESTION -> ServerValues.POST_TAG_APP_QUESTION
        PostTag.OTHER_LOW -> ServerValues.POST_TAG_OTHER_LOW
        PostTag.SEX_CHAT -> ServerValues.POST_TAG_SEX_CHAT
    }
}
