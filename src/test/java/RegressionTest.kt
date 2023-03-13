import enums.MessageTypes
import enums.ModerationDecisionType
import enums.ModerationRejectionSeverity
import enums.PostTag
import org.joda.time.DateTime
import org.testng.Assert
import org.testng.annotations.Test
import kotlin.collections.HashMap

class RegressionTest : SmokeTest() {

    @Test(dependsOnMethods = ["rejectComment"])
    fun unarchiveGroupChat() {

        startTest("unarchiveGroupChat")

        val feedDocId = firstUserId + "_" + chatID

        val responseForUnarchivingChat = home.unarchiveFeedItem(
            firstAppUserJwtToken,
            feedDocId
        )

        Assert.assertTrue(
            responseForUnarchivingChat.code == 200,
            "Wrong status code for unarchiving group chat: ${responseForUnarchivingChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnarchivingChat, "unarchiving group chat")

        val result: Boolean = responseForUnarchivingChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "The feed did not unarchive"
        )
        println("The feed doc '$feedDocId' was successfully unarchived!")

        onTestPassed("unarchiveGroupChat")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun setSuggestedChatViewed () {

        startTest("setSuggestedChatSeen")

        val responseForSettingSuggestedChatViewed = suggestedFeed.setSuggestedChatViewed(
            secondAppUserJwtToken,
            chatID!!
        )

        Assert.assertTrue(
            responseForSettingSuggestedChatViewed.code == 200,
            "Wrong status code for setting suggested chat viewed: ${responseForSettingSuggestedChatViewed.code}"
        )

        logs.printExtensiveTechnicalLogs(responseForSettingSuggestedChatViewed, "setting suggested chat viewed")

        val result: HashMap<*, *> =  responseForSettingSuggestedChatViewed.body("result")

        Assert.assertTrue(
            result.size != 0,
            "The chat wasn't viewed."
        )

        onTestPassed("setSuggestedChatViewed")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun setSuggestedChatsSeen() {

        startTest("setSuggestedChatSeen")

        val responseForSettingSuggestedChatSeen = suggestedFeed.setSuggestedChatsSeen(
            secondAppUserJwtToken,
            arrayOf(chatID)
        )

        Assert.assertTrue(
            responseForSettingSuggestedChatSeen.code == 200,
            "Wrong status code for setting suggested chat seen: ${responseForSettingSuggestedChatSeen.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForSettingSuggestedChatSeen, "setting suggested chat seen")

        val result: Boolean = responseForSettingSuggestedChatSeen.body("result.data.success")

        Assert.assertTrue(
            result,
            "The chat wasn't seen."
        )

        onTestPassed("setSuggestedChatSeen")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun clearUnreadBadge() {

        startTest("clearUnreadBadge")

        val responseForClearingUnreadBadge = chatID?.let { id ->
            home.clearUnreadMessageCount(
                firstAppUserJwtToken,
                id
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForClearingUnreadBadge.code == 200,
            "Wrong status code for clearing unread badge: ${responseForClearingUnreadBadge.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForClearingUnreadBadge, "clearing unread badge")

        val result: Boolean = responseForClearingUnreadBadge.body("result.data.success")

        Assert.assertTrue(
            result,
            "The message badge wasn't cleared."
        )

        onTestPassed("clearUnreadBadge")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun archiveDmChat() {

        startTest("archiveDmChat")

        val dmFeedDoc = firstUserId + "_" + dmChatID

        val responseForArchivingDmChat = home.archiveFeedItem(
            firstAppUserJwtToken,
            dmFeedDoc
        )

        Assert.assertTrue(
            responseForArchivingDmChat.code == 200,
            "Incorrect status code for archiving dm chat: ${responseForArchivingDmChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForArchivingDmChat, "archiving dm chat")

        val result: Boolean = responseForArchivingDmChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "DM chat wasn't archived."
        )

        onTestPassed("archiveDmChat")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun unarchiveDmChat() {

        startTest("archiveDmChat")

        val dmFeedDoc = firstUserId + "_" + dmChatID

        val responseForUnarchivingDmChat = home.unarchiveFeedItem(
            firstAppUserJwtToken,
            dmFeedDoc
        )

        Assert.assertTrue(
            responseForUnarchivingDmChat.code == 200,
            "Incorrect status code for unarchiving dm chat: ${responseForUnarchivingDmChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnarchivingDmChat, "unarchiving dm chat")

        val result: Boolean = responseForUnarchivingDmChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "DM chat wasn't archived."
        )

        onTestPassed("unarchiveDmChat")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun followSuggestedChatBySendingMessage() {

        startTest("followSuggestedChatBySendingMessage")

        var chatId: String? = null

        val createFirstCommentResponse = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            chatTitle,
            chatId
        )

        Assert.assertTrue(
            createFirstCommentResponse.code == 200,
            "Incorrect status code for creating the first comment (post): ${createFirstCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(createFirstCommentResponse, "creating the first comment (post)")

        chatId = createFirstCommentResponse.body("result.data.postId")
        println("New post created: $chatId")

        Thread.sleep(10000)
        val responseForGettingSuggestedChats = suggestedFeed.getSuggestedChats(
            secondAppUserJwtToken,
            40
        )

        Assert.assertTrue(
            responseForGettingSuggestedChats.code == 200,
            "Wrong status code for getting suggested chats: ${responseForGettingSuggestedChats.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingSuggestedChats, "getting suggested chats")

        val responseForFollowingSuggestedChatBySendingMessage = chatId?.let { id ->
            suggestedFeed.followChatBySendingMessage(
                secondAppUserJwtToken,
                "followSuggestedChatBySendingMessage test",
                secondUserName,
                id
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForFollowingSuggestedChatBySendingMessage.code == 200,
            "Wrong status code for following suggested chats by sending a message:" +
                    "${responseForFollowingSuggestedChatBySendingMessage.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForFollowingSuggestedChatBySendingMessage,
            "following suggested chats by sending a message")

        val commentForFollowingChat: String = responseForFollowingSuggestedChatBySendingMessage.body(
            "result.data.comment.id"
        )

        Assert.assertNotNull(
            commentForFollowingChat,
            "The comment wasn't created."
        )

        onTestPassed("followSuggestedChatBySendingMessage")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun getChatParticipants() {

        startTest("getChatParticipants")

        val responseForGettingChatParticipants = chatID?.let { id ->
            comments.getChatParticipants(
                firstAppUserJwtToken,
                id
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForGettingChatParticipants.code == 200,
            "Wrong status code for getting chat participants: ${responseForGettingChatParticipants.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingChatParticipants, "getting chat participants")

        val result: ArrayList<Any> = responseForGettingChatParticipants.body("result.data.participants.name")

        Assert.assertTrue(
             result[1] == secondUserName,
            "There is no such user in the chat"
        )

        onTestPassed("getChatParticipants")
    }

//    @Test(dependsOnMethods = ["deleteComment"])
//    fun setUserFacebookData() {
//        startTest("setContacts")
//
//        val settingUserFbDataResponse = onboarding.setUserFacebookData(appUserJwtToken)
//
//        Assert.assertTrue(
//            settingUserFbDataResponse.code == 200,
//            "Wrong status code for setting user's facebook data: ${settingUserFbDataResponse.code}"
//        )
//        println("Status code for setting user's facebook data is: ${settingUserFbDataResponse.code}")
//        println("The body we received is: ${settingUserFbDataResponse.body}")
//
//        // Waiting while Nikita finishes integrating this handler on his side to know more info.
//
//        onTestPassed("setUserFacebookData")
//    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun createSmsInvites() {

        startTest("createSmsInvites")

        val responseForCreatingSmsInvite = externalIntegration.createSmsInvites(firstAppUserJwtToken)

        Assert.assertTrue(
            responseForCreatingSmsInvite.code == 200,
            "Wrong status code while SMS sending: ${responseForCreatingSmsInvite.code}"

        )
        logs.printExtensiveTechnicalLogs(responseForCreatingSmsInvite, "creating SMS invites")

        val resultBody: HashMap<*, *> = responseForCreatingSmsInvite.body("result.data")

        Assert.assertTrue(
            resultBody.size == 0,
            "Null object detect when you try to send invite SMS"
        )

        onTestPassed("createSmsInvites")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun setContactsForAds() {

        startTest("setContactsForAds")

        val responseForSettingContactForAdds = externalIntegration.setContactsForADS(firstAppUserJwtToken)

        Assert.assertTrue(
            responseForSettingContactForAdds.code == 200,
            "Wrong status code while setting contacts: ${responseForSettingContactForAdds.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForSettingContactForAdds, "setting contacts")

        val resultBody: HashMap<*, *> = responseForSettingContactForAdds.body("result.data")
        Assert.assertTrue(
            resultBody.size == 0,
            "Null object detect when you try to set contacts for ads."
        )

        onTestPassed("setContactsForAds")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun createAppReview() {

        startTest("createAppReview")

        val responseForCreatingAppReview = externalIntegration.createAppReview(
            firstAppUserJwtToken,
            "This app is something else",
            5
        )

        Assert.assertTrue(
            responseForCreatingAppReview.code == 200,
            "Wrong status code for creating app review: ${responseForCreatingAppReview.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingAppReview, "creating an app review")

        val result: Boolean = responseForCreatingAppReview.body("result.data.success")

        Assert.assertTrue(
            result,
            "The app review is not created"
        )

        onTestPassed("createAppReview")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun acknowledgeCommentsRejection() {

        startTest("acknowledgeCommentsRejection")

        val responseForCreatingComment = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            "acknowledgeCommentsRejection test moderate",
            chatID
        )

        Assert.assertTrue(
            responseForCreatingComment.code == 200,
            "Wrong status code for creating a comment: ${responseForCreatingComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingComment, "creating a comment")

        val commentID: String = responseForCreatingComment.body("result.data.id")

        Thread.sleep(4000)
        val responseForRejectingComment = externalIntegration.rejectContent(
            supervisorJwtToken,
            commentID,
            MessageTypes.COMMENT,
            ModerationDecisionType.REJECTED,
            "acknowledgeCommentsRejection test",
            "Other",
            ModerationRejectionSeverity.LOW
        )

        Assert.assertTrue(
            responseForRejectingComment.code == 200,
            "Wrong status code for rejecting a comment: ${responseForRejectingComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForRejectingComment, "rejecting a comment")

        val responseForAcknowledgingCommentsRejection = comments.acknowledgeCommentsRejection(
            firstAppUserJwtToken,
            commentID
        )

        Assert.assertTrue(
            responseForAcknowledgingCommentsRejection.code == 200,
            "Wrong status code for acknowledging comment's rejection: ${responseForAcknowledgingCommentsRejection.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForAcknowledgingCommentsRejection, "acknowledging comment's rejection")

        val result: Boolean = responseForAcknowledgingCommentsRejection.body("result.data.success")

        Assert.assertTrue(
            result,
            "There are no deleted comment with ID = $commentID"
        )

        onTestPassed("acknowledgeCommentsRejection")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun acknowledgeDMRejection() {

        startTest("acknowledgeDMRejection")

        val responseForDmRejection = lastCreatedDmID?.let {
            externalIntegration.rejectContent(
                supervisorJwtToken,
                it,
                MessageTypes.DIRECT_MESSAGE,
                ModerationDecisionType.REJECTED,
                "acknowledgeDMRejection test",
                "Other",
                ModerationRejectionSeverity.LOW
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForDmRejection.code == 200,
            "Wrong status code for rejecting DM chat: ${responseForDmRejection.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForDmRejection, "rejecting DM chat")

        val responseForAcknowledgingDmRejection = lastCreatedDmID?.let {
            comments.acknowledgeDMRejection(
                firstAppUserJwtToken,
                it
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForAcknowledgingDmRejection.code == 200,
            "Wrong status code for acknowledging DM rejection: ${responseForAcknowledgingDmRejection.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForAcknowledgingDmRejection, "acknowledging DM rejection")

        val result: Boolean = responseForAcknowledgingDmRejection.body("result.data.success")

        Assert.assertTrue(
            result,
            "There is no deleted DM with ID = $dmChatID"
        )

        onTestPassed("acknowledgeDMRejection")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun setUserLastSession() {

        startTest("setUserLastSession")

        val time = DateTime.now().toDateTime().toDate().toInstant().toEpochMilli()

        val responseForSettingUserLastSession = externalIntegration.setUserLastSession(
            firstAppUserJwtToken,
            time
        )

        Assert.assertTrue(
            responseForSettingUserLastSession.code == 200,
            "Wrong status code for setting user's last session: ${responseForSettingUserLastSession.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForSettingUserLastSession, "setting user's last session")

        val result: Boolean = responseForSettingUserLastSession.body("result.data.success")

        Assert.assertTrue(
            result,
            "Last session is not set Time = $time"
        )

        onTestPassed("setUserLastSession")
    }

    @Test(dependsOnMethods = ["rejectComment"])
    fun getRatingPromptness() {

        startTest("getRatingPromptness")

        val responseForGettingRatingPromptness = externalIntegration.getRatingPromptness(
            firstAppUserJwtToken,
            "Algo_1"
        )

        Assert.assertTrue(
            responseForGettingRatingPromptness.code == 200,
            "Wrong status code for getting rating promptness: ${responseForGettingRatingPromptness.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingRatingPromptness, "getting rating promptness")

        val isReady: Boolean = responseForGettingRatingPromptness.body("result.data.isReady")

        Assert.assertFalse(
            isReady,
            "User is ready to show them rating prompt"
        )

        onTestPassed("getRatingPromptness")
    }

    @Test(dependsOnMethods = ["getRatingPromptness"])
    fun deleteUser() {

        startTest("deleteUser")

        val responseForDeletingUser = externalIntegration.deleteUser(firstAppUserJwtToken)

        Assert.assertTrue(
            responseForDeletingUser.code == 200,
            "Wrong status code for deleting user: ${responseForDeletingUser.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForDeletingUser, "deleting user")

        val result: Boolean = responseForDeletingUser.body("result.data.success")

        Assert.assertTrue(
            result,
            "The user wasn't deleted!"
        )

        onTestPassed("deleteUser")
    }
}