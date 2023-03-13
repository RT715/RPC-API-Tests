import enums.MessageTypes
import enums.ModerationDecisionType
import enums.ModerationRejectionSeverity
import enums.PostTag
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import java.util.Objects

open class SmokeTest : Settings() {

    @BeforeTest
    fun getSupervisorJwtToken() {
        val response = auth.getJwtToken(supervisorID)

        Assert.assertTrue(
            response.code == 200,
            "Incorrect status code: ${response.code}"
        )
        logs.printExtensiveTechnicalLogs(response, "getting JWT token for supervisor")

        supervisorJwtToken = response.body("idToken")
    }

    @BeforeTest(dependsOnMethods = ["getSupervisorJwtToken"])
    fun createFirstTestUser() {
        val createTestUserResponse = auth.createTestUser(Credentials.FIRST_TEST_USER_PHONE_NUMBER)

        Assert.assertTrue(
            createTestUserResponse.code == 200,
            "Incorrect status code: ${createTestUserResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(createTestUserResponse, "creating the first test user")

        firstUserId = createTestUserResponse.body("result.data.uid")
        println("New created user: $firstUserId")

        Thread.sleep(10000)
        val responseForGettingUserIdByPhone = auth.getUserUIDByPlainPhone(
            supervisorJwtToken,
            Credentials.FIRST_TEST_USER_PHONE_NUMBER
        )

        Assert.assertTrue(
            responseForGettingUserIdByPhone.code == 200,
            "Incorrect status code for getting user's id by phone: ${responseForGettingUserIdByPhone.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingUserIdByPhone, "getting user's id by phone")

        val userIdGettingByPhone: String = responseForGettingUserIdByPhone.body("result.data.uid")

        Assert.assertEquals(
            userIdGettingByPhone,
            firstUserId,
            "User ID received by number is wrong."
        )

        val getDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_USERS,
            firstUserId
        )

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Incorrect status code: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting the user's document")

        Assert.assertNotNull(
            getDocumentResponse,
            "There is no body for getting document."
        )
    }

    @BeforeTest(dependsOnMethods = ["createFirstTestUser"])
    fun createSecondTestUser() {
        val createTestUserResponse = auth.createTestUser(Credentials.SECOND_TEST_USER_PHONE_NUMBER)

        Assert.assertTrue(
            createTestUserResponse.code == 200,
            "Incorrect status code: ${createTestUserResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(createTestUserResponse, "creating the second test user")

        secondUserId = createTestUserResponse.body("result.data.uid")
        println("New created user: $secondUserId")

        Thread.sleep(10000)
        val responseForGettingUserIdByPhone = auth.getUserUIDByPlainPhone(
            supervisorJwtToken,
            Credentials.SECOND_TEST_USER_PHONE_NUMBER
        )

        Assert.assertTrue(
            responseForGettingUserIdByPhone.code == 200,
            "Incorrect status code for getting user's id by phone: ${responseForGettingUserIdByPhone.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingUserIdByPhone, "getting user's id by phone")

        val userIdGettingByPhone: String = responseForGettingUserIdByPhone.body("result.data.uid")

        Assert.assertEquals(
            userIdGettingByPhone,
            secondUserId,
            "User ID received by number is wrong."
        )

        val getDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_USERS,
            secondUserId
        )

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Incorrect status code: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting user's document")

        Assert.assertNotNull(getDocumentResponse, "There is no body for getting document.")
    }

    @BeforeTest(dependsOnMethods = ["createSecondTestUser"])
    fun getFirstAppUserJwtToken() {
        val response = auth.getJwtToken(firstUserId)

        Assert.assertTrue(
            response.code == 200,
            "Incorrect status code: ${response.code}"
        )
        logs.printExtensiveTechnicalLogs(response, "getting JWT token for the first test user")

        firstAppUserJwtToken = response.body("idToken")
    }

    @BeforeTest(dependsOnMethods = ["getFirstAppUserJwtToken"])
    fun getSecondAppUserJwtToken() {
        val response = auth.getJwtToken(secondUserId)

        Assert.assertTrue(
            response.code == 200,
            "Incorrect status code: ${response.code}"
        )
        logs.printExtensiveTechnicalLogs(response, "getting JWT token for the second test user")

        secondAppUserJwtToken = response.body("idToken")
    }

    @Test
    fun setUserAAge() {

        startTest("setUserAge")

        val userAgeToSet = 28

        // Sending POST request to set the user's age
        val setUserAgeResponse = onboarding.setUserAge(
            firstAppUserJwtToken,
            userAgeToSet
        )

        // Verifying the response with the expected status code
        Assert.assertTrue(
            setUserAgeResponse.code == 200,
            "Incorrect status code for setting up the user's age: ${setUserAgeResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(setUserAgeResponse, "setting the user's age")

        // Sending POST request to get the whole document from Firestore
        val getUserResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_USERS,
            firstUserId
        )
        // Verifying the response with the expected status code
        Assert.assertTrue(
            getUserResponse.code == 200,
            "Incorrect status code when getting the user's document from database: ${getUserResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getUserResponse, "getting the user's document")

        // Getting user's age from the body we received from response2 and storing it into the userAgeInDB variable
        val userAgeInDB: Int = getUserResponse.body("result.data.age")
        println("User's age we set up is: $userAgeToSet")
        println("User's age in DB: $userAgeInDB")

        // Verifying the user's age we set up equals the user's age in database
        Assert.assertEquals(
            userAgeInDB,
            userAgeToSet,
            "Users' ages are different!"
        )

        onTestPassed("setUserAge")
    }

    @Test(dependsOnMethods = ["setUserAAge"])
    fun setUserBAge() {

        startTest("setUserAge")

        val userAgeToSet = 28

        // Sending POST request to set the user's age
        val setUserAgeResponse = onboarding.setUserAge(
            secondAppUserJwtToken,
            userAgeToSet
        )

        // Verifying the response with the expected status code
        Assert.assertTrue(
            setUserAgeResponse.code == 200,
            "Incorrect status code for setting up the user's age: ${setUserAgeResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(setUserAgeResponse, "setting the user's age")

        // Sending POST request to get the whole document from Firestore
        val getUserResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_USERS,
            secondUserId
        )
        // Verifying the response with the expected status code
        Assert.assertTrue(
            getUserResponse.code == 200,
            "Incorrect status code when getting the user's document from database: ${getUserResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getUserResponse, "getting the user's document")

        // Getting user's age from the body we received from response2 and storing it into the userAgeInDB variable
        val userAgeInDB: Int = getUserResponse.body("result.data.age")
        println("User's age we set up is: $userAgeToSet")
        println("User's age in DB: $userAgeInDB")

        // Verifying the user's age we set up equals the user's age in database
        Assert.assertEquals(
            userAgeInDB,
            userAgeToSet,
            "Users' ages are different!"
        )

        onTestPassed("setUserBAge")
    }

    @Test(dependsOnMethods = ["setUserBAge"])
    fun setUserFcmToken() {

        startTest("setUserFcmToken")

        val setUserFcmTokenResponse = onboarding.setUserFcmToken(
            firstAppUserJwtToken,
            Credentials.FCM_TOKEN
        )

        Assert.assertTrue(
            setUserFcmTokenResponse.code == 200,
            "Incorrect status code for setting user's FCM token: ${setUserFcmTokenResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(setUserFcmTokenResponse, "setting user's FCM token")

        val getUserDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_USERS,
            firstUserId
        )

        Assert.assertTrue(
            getUserDocumentResponse.code == 200,
            "Incorrect status code when getting the user's document from database: ${getUserDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getUserDocumentResponse, "getting the user's document")


        val userFcmTokenInDB: String = getUserDocumentResponse.body("result.data.fcmToken")
        println("User's FCM token in DB is: $userFcmTokenInDB")

        Assert.assertEquals(
            Credentials.FCM_TOKEN,
            userFcmTokenInDB,
            "User's FCM tokens are different!"
        )

        onTestPassed("setUserFcmToken")
    }

    @Test(dependsOnMethods = ["setUserFcmToken"])
    fun setUserIp() {

        startTest("setUserIp")

        val responseForSettingIp = onboarding.setIp(firstAppUserJwtToken)

        Assert.assertTrue(
            responseForSettingIp.code == 200,
            "Incorrect status code for setting ip: ${responseForSettingIp.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForSettingIp, "setting user's ip")

        val userIp: String = responseForSettingIp.body("result.data")

        val getUserDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_USERS,
            firstUserId
        )

        Assert.assertTrue(
            getUserDocumentResponse.code == 200,
            "Incorrect status code when getting the user's document from database: ${getUserDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getUserDocumentResponse, "getting the user's document")


        val userIpInDB: String = getUserDocumentResponse.body("result.data.ip")
        println("User's ip in DB is: $userIpInDB")

        Assert.assertEquals(
            userIpInDB,
            userIp,
            "User's ips don't match"
        )

        onTestPassed("setIp")
    }

    @Test(dependsOnMethods = ["setUserIp"])
    fun getSharedPromptsAndCreateChat() {

        startTest("getSharedPromptsAndCreateChat")

        val responseForGettingSharedPrompts = onboarding.getSharingPrompts(firstAppUserJwtToken)

        Assert.assertTrue(
            responseForGettingSharedPrompts.code == 200,
            "Incorrect status code for getting shared prompts: ${responseForGettingSharedPrompts.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingSharedPrompts, "getting shared prompts")

        val promptsText: ArrayList<String> = responseForGettingSharedPrompts.body("result.data.items.prompt")

        Assert.assertTrue(
            promptsText.isNotEmpty(),
            "No prompts were fetched."
        )

        val responseForCreatingSharedChat = onboarding.createSharedChat(
            firstAppUserJwtToken,
            promptsText[0]
        )

        Assert.assertTrue(
            responseForCreatingSharedChat.code == 200,
            "Incorrect status code for creating shared chat: ${responseForCreatingSharedChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingSharedChat, "creating shared chat")

        sharedChatID = responseForCreatingSharedChat.body("result.data.chatId")

        val result: HashMap<*, *> = responseForCreatingSharedChat.body("result.data")

        Assert.assertTrue(
            result.size != 0,
            "Chat wasn't created."
        )

        onTestPassed("getSharedPromptsAndCreateChat")
    }

    @Test(dependsOnMethods = ["getSharedPromptsAndCreateChat"])
    fun getSharedChatFeed() {

        startTest("getSharedChatFeed")

        val responseForGettingSharedChatFeed = externalIntegration.getSharedChatFeed(
            firstAppUserJwtToken,
            sharedChatID
        )

        Assert.assertTrue(
            responseForGettingSharedChatFeed.code == 200,
            "Incorrect status code for getting shared chat feed: ${responseForGettingSharedChatFeed.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingSharedChatFeed, "getting shared chat feed")

        val result: HashMap<*, *> = responseForGettingSharedChatFeed.body("result.data")

        Assert.assertTrue(
            result.size != 0,
            "Couldn't get shared chat's feed."
        )

        onTestPassed("getSharedChatFeed")
    }

    @Test(dependsOnMethods = ["getSharedChatFeed"])
    fun getSharedChatInfo() {

        startTest("getSharedChatInfo")

        val responseForGettingSharedChatInfo = externalIntegration.getSharedChatInfo(
            sharedChatID,
            anonToken
        )

        Assert.assertTrue(
            responseForGettingSharedChatInfo.code == 200,
            "Incorrect status code for getting shared chat feed: ${responseForGettingSharedChatInfo.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingSharedChatInfo, "getting shared chat feed")

        val result: HashMap<*, *> = responseForGettingSharedChatInfo.body("result.data.chatInfo")

        Assert.assertTrue(
            result.size != 0,
            "No info about a chat."
        )

        onTestPassed("getSharedChatInfo")
    }

    @Test(dependsOnMethods = ["getSharedChatInfo"])
    fun createAnonComment() {

        startTest("createAnonComment")

        val responseForCreatingAnonComment = comments.createAnonComment(
            sharedChatID,
            "Creating anon comment test",
            anonToken
        )

        Assert.assertTrue(
            responseForCreatingAnonComment.code == 200,
            "Incorrect status code for creating anon comment: ${responseForCreatingAnonComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingAnonComment, "creating anon comment")

        anonToken = responseForCreatingAnonComment.body("result.data.anonToken")

        val result: HashMap<*, *> = responseForCreatingAnonComment.body("result.data.")

        Assert.assertTrue(
            result.size != 0,
            "The comment wasn't created."
        )

        onTestPassed("createAnonComment")
    }

    @Test(dependsOnMethods = ["createAnonComment"])
    fun publishAnonComments() {

        startTest("publishAnonComments")

        val responseForPublishingAnonComments = externalIntegration.publishAnonComments(
            firstAppUserJwtToken,
            anonToken
        )

        Assert.assertTrue(
            responseForPublishingAnonComments.code == 200,
            "Incorrect status code for creating anon comment: ${responseForPublishingAnonComments.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForPublishingAnonComments, "creating anon comment")

        val result: HashMap<*, *> = responseForPublishingAnonComments.body("result.data")

        Assert.assertTrue(
            result.size == 0,
            "The comment wasn't published."
        )

        onTestPassed("publishAnonComments")
    }

    @Test(dependsOnMethods = ["publishAnonComments"])
    fun createComment() {

        startTest("createComment")

        val createFirstCommentResponse = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            chatTitle,
            chatID
        )

        Assert.assertTrue(
            createFirstCommentResponse.code == 200,
            "Incorrect status code for creating the first comment (post): ${createFirstCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(createFirstCommentResponse, "creating the first comment (post)")

        chatID = createFirstCommentResponse.body("result.data.postId")
        println("New post created: $chatID")
        lastCreatedCommentID = createFirstCommentResponse.body("result.data.id")
        println("Last comment's ID is: $lastCreatedCommentID")
        lastCreatedCommentText = createFirstCommentResponse.body("result.data.text")

        val getDocumentResponse = lastCreatedCommentID?.let { id ->
            documents.getDocument(
                ServerValues.COLLECTION_NAME_COMMENTS,
                id
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Incorrect status code for getting document for last created comment: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting document for the comment we created")


        lastCreatedCommentTextInDB = getDocumentResponse.body("result.data.text")
        println("Comment's text we created: $lastCreatedCommentText")
        println("Comment's text in DB: $lastCreatedCommentTextInDB")

        Assert.assertEquals(
            lastCreatedCommentText,
            lastCreatedCommentTextInDB,
            "The comment's text we create is different from the comment's text in DB"
        )

        onTestPassed("createComment")
    }

    @Test(dependsOnMethods = ["createComment"])
    fun createComplaint() {

        startTest("createComplaint")

        val responseForCreatingComplaint = lastCreatedCommentID?.let { id ->
            comments.createComplaint(
                firstAppUserJwtToken,
                "Other",
                id,
                MessageTypes.COMMENT,
                "Some text"
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForCreatingComplaint.code == 200,
            "Wrong status code for creating complaint: ${responseForCreatingComplaint.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingComplaint, "creating complaint")

        val result: Boolean = responseForCreatingComplaint.body("result.data.success")

        Assert.assertTrue(
            result,
            "The complaint hasn't been created"
        )

        onTestPassed("createComplaint")
    }

    @Test(dependsOnMethods = ["createComplaint"])
    fun muteGroupChat() {

        startTest("muteGroupChat")
        Thread.sleep(15000)

        val feedDocId = firstUserId + "_" + chatID

        val responseForMutingGroupChat = home.muteFeedItem(
            firstAppUserJwtToken,
            feedDocId
        )

        Assert.assertTrue(
            responseForMutingGroupChat.code == 200,
            "Wrong status code for muting group chat: ${responseForMutingGroupChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForMutingGroupChat, "muting group chat")

        val result: Boolean = responseForMutingGroupChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "The feed didn't mute"
        )
        println("The feed doc '$feedDocId' was successfully muted")

        onTestPassed("muteGroupChat")
    }

    @Test(dependsOnMethods = ["createComplaint"])
    fun unmuteGroupChat() {

        startTest("unmuteGroupChat")

        val feedDocId = firstUserId + "_" + chatID

        val responseForUnmutingGroupChat = home.unmuteFeedItem(
            firstAppUserJwtToken,
            feedDocId
        )

        Assert.assertTrue(
            responseForUnmutingGroupChat.code == 200,
            "Wrong status code for unmuting group chat: ${responseForUnmutingGroupChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnmutingGroupChat, "unmuting group chat")

        val result: Boolean = responseForUnmutingGroupChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "The feed didn't unmute"
        )
        println("The feed doc '$feedDocId' was successfully unmuted")

        onTestPassed("unmuteGroupChat")
    }

    @Test(dependsOnMethods = ["muteGroupChat"])
    fun archiveGroupChat() {

        startTest("archiveGroupChat")

        val feedDocId = firstUserId + "_" + chatID

        val responseForArchivingChat = home.archiveFeedItem(
            firstAppUserJwtToken,
            feedDocId
        )

        Assert.assertTrue(
            responseForArchivingChat.code == 200,
            "Wrong status code for archiving group chat: ${responseForArchivingChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForArchivingChat, "archiving group chat")

        val result: Boolean = responseForArchivingChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "The feed did not archive"
        )
        println("The feed doc '$feedDocId' was successfully archived")

        onTestPassed("archiveGroupChat")
    }

    @Test(dependsOnMethods = ["archiveGroupChat"])
    fun setContacts() {

        startTest("setContacts")

        val response = externalIntegration.setContacts(firstAppUserJwtToken)

        Assert.assertTrue(
            response.code == 200,
            "Wrong status code while SMS sending: ${response.code}"
        )
        logs.printExtensiveTechnicalLogs(response, "setting contacts")

        val resultBody: Boolean = response.body("result.data.success")

        Assert.assertTrue(
            resultBody,
            "Null object detect when you try to set contacts"
        )

        onTestPassed("createSmsInvites")
    }

    @Test(dependsOnMethods = ["setContacts"])
    fun initFeed() {

        startTest("initFeed")

        val responseForInitiatingFeed = onboarding.initFeed(
            firstAppUserJwtToken,
            28,
            arrayOf(Credentials.FIRST_TEST_USER_PHONE_NUMBER, Credentials.SECOND_TEST_USER_PHONE_NUMBER)
        )

        Assert.assertTrue(
            responseForInitiatingFeed.code == 200,
            "Wrong status code for initiating users' feed: ${responseForInitiatingFeed.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForInitiatingFeed, "initiating users' feed")

        val result: Boolean = responseForInitiatingFeed.body("result.data.success")

        Assert.assertTrue(
            result,
            "The feed wasn't initialised"
        )

        onTestPassed("initFeed")
    }

    @Test(dependsOnMethods = ["initFeed"])
    fun newGroupChatMutedVerification() {

        startTest("newGroupChatMutedVerification")

        val responseForPalUpUsers = externalIntegration.palUpUsers(
            firstAppUserJwtToken,
            arrayOf(Credentials.SECOND_TEST_USER_PHONE_NUMBER)
        )

        Assert.assertTrue(
            responseForPalUpUsers.code == 200,
            "Wrong status code for palling up users: ${responseForPalUpUsers.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForPalUpUsers, "palling up users")

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

        val responseForGettingSuggestedChat = suggestedFeed.getSuggestedChat(
            secondAppUserJwtToken,
            chatID!!
        )

        Assert.assertTrue(
            responseForGettingSuggestedChat.code == 200,
            "Wrong status code for getting suggested chat: ${responseForGettingSuggestedChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingSuggestedChat, "getting suggested chat")

        val responseForFollowingSuggestedChat = suggestedFeed.followSuggestedChat(
            secondAppUserJwtToken,
            chatID!!
        )

        Assert.assertTrue(
            responseForFollowingSuggestedChat.code == 200,
            "Wrong status code for following suggested chat: ${responseForFollowingSuggestedChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForFollowingSuggestedChat, "following suggested chat")

        val secondUserFeedDoc = secondUserId + "_" + chatID

        Thread.sleep(10000)
        val getDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_FEED,
            secondUserFeedDoc
        )

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Incorrect status code for getting document: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting feed doc document")

        val mutedState: String = getDocumentResponse.body("result.data.mutedState")

        Assert.assertEquals(
            mutedState,
            ServerValues.VALUE_STATE_NEVER_MUTED,
            "The value of mutedState is 'unmuted'."
        )
        println("The value state in mutedState field is '$mutedState'")

        onTestPassed("newGroupChatMutedVerification")
    }

    @Test(dependsOnMethods = ["newGroupChatMutedVerification"])
    fun setChatTitle() {

        startTest("setChatTitle")

        val responseForSettingChatTitle = chatID?.let { id ->
            comments.setTitle(
                firstAppUserJwtToken,
                id,
                "Chat title by API"
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForSettingChatTitle.code == 200,
            "Wrong status code for setting chat title: ${responseForSettingChatTitle.code}"

        )
        logs.printExtensiveTechnicalLogs(responseForSettingChatTitle, "setting chat title")

        val result: Boolean = responseForSettingChatTitle.body("result.data.success")

        Assert.assertTrue(
            result,
            "The chat title was not set up."
        )

        val titleId: String = responseForSettingChatTitle.body("result.data.titleId")

        val responseForApprovingTitle = externalIntegration.approveTitle(
            supervisorJwtToken,
            chatID!!,
            titleId,
            ModerationDecisionType.APPROVED,
            "Test approving title"
        )

        Assert.assertTrue(
            responseForApprovingTitle.code == 200,
            "Wrong status code for approving chat title: ${responseForApprovingTitle.code}"

        )
        logs.printExtensiveTechnicalLogs(responseForApprovingTitle, "approving chat title")

        onTestPassed("setChatTitle")
    }

    @Test(dependsOnMethods = ["setChatTitle"])
    fun unfollowSuggestedChat() {

        startTest("unfollowSuggestedChat")

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

        Thread.sleep(7000)
        val responseForSettingPostTag = chatId?.let { id ->
            externalIntegration.setPostTag(
                supervisorJwtToken,
                id,
                PostTag.SELF_DISCLOSURE
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForSettingPostTag.code == 200,
            "Wrong status code for setting up post tag"
        )
        logs.printExtensiveTechnicalLogs(responseForSettingPostTag, "setting up post tag")

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

        val responseForFollowingSuggestedChat = suggestedFeed.followSuggestedChat(
            secondAppUserJwtToken,
            chatId
        )

        Assert.assertTrue(
            responseForFollowingSuggestedChat.code == 200,
            "Wrong status code for following suggested chat: ${responseForFollowingSuggestedChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForFollowingSuggestedChat, "following suggested chat")

        val responseForUnfollowingSuggestedChat = suggestedFeed.unfollowSuggestedChat(
            secondAppUserJwtToken,
            chatId
        )

        Assert.assertTrue(
            responseForUnfollowingSuggestedChat.code == 200,
            "Wrong status code for unfollowing suggested chat: ${responseForUnfollowingSuggestedChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnfollowingSuggestedChat, "unfollowing suggested chat")

        val resultBody: HashMap<*, *> = responseForUnfollowingSuggestedChat.body("result.data")

        Assert.assertTrue(
            resultBody.size != 0,
            "The chat wasn't unfollowed."
        )

        onTestPassed("unfollowSuggestedChat")

    }

    @Test(dependsOnMethods = ["unfollowSuggestedChat"])
    fun dmAfterFollowingChat() {

        startTest("dmAfterFollowingChat")

        var chatId: String? = null

        val responseForCreatingNewChat = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            "dmAfterFollowingChat",
            chatId
        )

        Assert.assertTrue(
            responseForCreatingNewChat.code == 200,
            "Wrong status code for creating a new chat: ${responseForCreatingNewChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingNewChat, "creating a new chat")

        chatId = responseForCreatingNewChat.body("result.data.postId")
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

        val responseForFollowingSuggestedChat = chatId?.let { id ->
            suggestedFeed.followSuggestedChat(
                secondAppUserJwtToken,
                id
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForFollowingSuggestedChat.code == 200,
            "Wrong status code for following suggested chat: ${responseForFollowingSuggestedChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForFollowingSuggestedChat, "following suggested chat")

        val creatingDmResponse = comments.createDM(
            secondAppUserJwtToken,
            secondUserName,
            firstUserName,
            "dmAfterFollowingChat",
            chatId
        )

        Assert.assertTrue(
            creatingDmResponse.code == 200,
            "Wrong status code for creating DM: ${creatingDmResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(creatingDmResponse, "creating DM")

        val result: Boolean = creatingDmResponse.body("result.data.success")

        Assert.assertTrue(
            result,
            "DM comment wasn't created"
        )

        onTestPassed("dmAfterFollowingChat")
    }

    @Test(dependsOnMethods = ["dmAfterFollowingChat"])
    fun getInitialChats() {

        startTest("getInitialChats")

        val responseForGettingInitialChats = suggestedFeed.getInitialChats(
            secondAppUserJwtToken,
            arrayOf(Credentials.FIRST_TEST_USER_PHONE_NUMBER),
            28,
            40
        )

        Assert.assertTrue(
            responseForGettingInitialChats.code == 200,
            "Wrong status code for getting initial chats: ${responseForGettingInitialChats.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingInitialChats, "getting initial chats")

        val result: ArrayList<Objects> = responseForGettingInitialChats.body("result.data.feedItems")

        Assert.assertNotNull(
            result,
            "Initial chats couldn't be received or there is no chats from other user."
        )

        onTestPassed("getInitialChats")

    }

    @Test(dependsOnMethods = ["getInitialChats"])
    fun createDirectMessage() {

        startTest("createDirectMessage")

        val createCommentResponse = comments.createComment(
            secondAppUserJwtToken,
            secondUserName,
            commentTitle,
            chatID
        )
        Thread.sleep(2000)

        Assert.assertTrue(
            createCommentResponse.code == 200,
            "Wrong status code for creating a comment: ${createCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(createCommentResponse, "creating a comment")

        lastCreatedCommentID = createCommentResponse.body("result.data.id")

        val creatingDmResponse = comments.createDM(
            firstAppUserJwtToken,
            firstUserName,
            secondUserName,
            "Creating first DM",
            chatID
        )

        Assert.assertTrue(
            creatingDmResponse.code == 200,
            "Wrong status code for creating DM: ${creatingDmResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(creatingDmResponse, "creating DM")

        dmChatID = creatingDmResponse.body("result.data.chatId")
        lastCreatedDmID = creatingDmResponse.body("result.data.id")
        println("New DM id is: $dmChatID")
        println("New DM comment id is: $lastCreatedDmID")

        val resultBody: HashMap<*, *> = creatingDmResponse.body("result.data")

        Assert.assertTrue(
            resultBody.size != 0,
            "Null object detected when you try to create a DM."
        )

        onTestPassed("createDirectMessage")
    }

    @Test(dependsOnMethods = ["createDirectMessage"])
    fun deleteDM(){

        startTest("deleteDM")

        val responseForCreatingNewDM = comments.createDM(
            firstAppUserJwtToken,
            firstUserName,
            secondUserName,
            "Deleting DM title",
            chatID
        )

        Assert.assertTrue(
            responseForCreatingNewDM.code == 200,
            "Wrong status code for creating DM: ${responseForCreatingNewDM.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingNewDM, "creating DM")

        val newDM: String = responseForCreatingNewDM.body("result.data.id")

        Thread.sleep(15000)
        val responseForDeletingDM = comments.deleteDM(
            firstAppUserJwtToken,
            newDM
        )

        Assert.assertTrue(
            responseForDeletingDM.code == 200,
            "Wrong status code for deleting DM: ${responseForDeletingDM.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForDeletingDM, "deleting DM")

        val result: Boolean = responseForDeletingDM.body("result.data.success")

        Assert.assertTrue(
            result,
            "Dm wasn't deleted"
        )

        onTestPassed("deleteDM")
    }

    @Test(dependsOnMethods = ["deleteDM"])
    fun muteDmChat() {

        startTest("muteDmChat")

        val dmFeedID = firstUserId + "_" + dmChatID

        Thread.sleep(15000)
        val responseForMutingDmChat = home.muteFeedItem(
            firstAppUserJwtToken,
            dmFeedID
        )

        Assert.assertTrue(
            responseForMutingDmChat.code == 200,
            "Wrong status code for muting DM chat: ${responseForMutingDmChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForMutingDmChat, "muting DM chat")

        val result: Boolean = responseForMutingDmChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "The DM feed did not mute"
        )
        println("The DM chat ID '$dmFeedID' was successfully muted")

        onTestPassed("muteDmChat")
    }

    @Test(dependsOnMethods = ["muteDmChat"])
    fun unmuteDmChat() {

        startTest("unmuteDmChat")

        val dmFeedID = firstUserId + "_" + dmChatID

        val responseForUnmutingDmChat = home.unmuteFeedItem(
            firstAppUserJwtToken,
            dmFeedID
        )

        Assert.assertTrue(
            responseForUnmutingDmChat.code == 200,
            "Wrong status code for unmuting DM chat: ${responseForUnmutingDmChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnmutingDmChat, "unmuting DM chat")

        val result: Boolean = responseForUnmutingDmChat.body("result.data.success")

        Assert.assertTrue(
            result,
            "The DM feed did not unmute"
        )

        println("The DM chat ID '$dmFeedID' was successfully unmuted")

        onTestPassed("unmuteDmChat")
    }

    @Test(dependsOnMethods = ["unmuteDmChat"])
    fun noPushInMutedGroupChatVerification() {

        startTest("noPushVerificationInMutedGroupChat")

        val feedDocID = secondUserId + "_" + chatID

        val responseForMutingGroupChat = home.muteFeedItem(
            secondAppUserJwtToken,
            feedDocID
        )

        Assert.assertTrue(
            responseForMutingGroupChat.code == 200,
            "Wrong status code for muting group chat: ${responseForMutingGroupChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForMutingGroupChat, "muting group chat")

        val responseForCreatingComment = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            commentTitle,
            chatID
        )

        Assert.assertTrue(
            responseForCreatingComment.code == 200,
            "Wrong status code for creating a new message: ${responseForCreatingComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingComment, "creating a new comment")

        lastCreatedCommentID = responseForCreatingComment.body("result.data.id")

        Thread.sleep(10000)
        val responseForGettingDocument = documents.getDocument(
            ServerValues.COLLECTION_NAME_TEST_FCM_NOTIFICATIONS,
            secondUserId
        )

        Assert.assertTrue(
            responseForGettingDocument.code == 200,
            "Wrong status code for getting the document: ${responseForGettingDocument.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForGettingDocument, "getting the document")

        val result: String = responseForGettingDocument.body("data")

        Assert.assertNull(
            result,
            "There is a push notification for a new message in the muted group chat"
        )

        onTestPassed("noPushInMutedGroupChatVerification")
    }

    @Test(dependsOnMethods = ["noPushInMutedGroupChatVerification"])
    fun noPushInMutedDmChatVerification() {

        startTest("noPushInMutedDmChatVerification")

        val responseForCreatingDmComment = comments.createDM(
            firstAppUserJwtToken,
            firstUserName,
            secondUserName,
            dmTitle,
            chatID
        )

        Assert.assertTrue(
            responseForCreatingDmComment.code == 200,
            "Wrong status code for creating a new DM comment: ${responseForCreatingDmComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingDmComment, "creating a new DM comment")

        lastCreatedDmID = responseForCreatingDmComment.body("result.data.id")
        lastCreatedCommentText = responseForCreatingDmComment.body("result.data.text")

        Thread.sleep(8000)

        val secondUserDmFeedID = secondUserId + "_" + dmChatID

        val getDocumentResponseForGettingSecondUserDmFeed = documents.getDocument(
            ServerValues.COLLECTION_NAME_FEED,
            secondUserDmFeedID
        )

        Assert.assertTrue(
            getDocumentResponseForGettingSecondUserDmFeed.code == 200,
            "Wrong status code for getting the second user's DM feed document: " +
                    "${getDocumentResponseForGettingSecondUserDmFeed.code}"
        )
        logs.printExtensiveTechnicalLogs(
            getDocumentResponseForGettingSecondUserDmFeed, "getting the second " +
                    "user's DM feed document"
        )

        lastCreatedCommentTextInDB = getDocumentResponseForGettingSecondUserDmFeed.body("result.data.body")

        Assert.assertEquals(
            lastCreatedCommentTextInDB,
            lastCreatedCommentText,
            "There is no comment in second user's DM feed"
        )

        val getDocumentResponseForTestNotifications = documents.getDocument(
            ServerValues.COLLECTION_NAME_TEST_FCM_NOTIFICATIONS,
            secondUserId
        )

        Assert.assertTrue(
            getDocumentResponseForTestNotifications.code == 200,
            "Wrong status code for getting the test FCM notifications document: " +
                    "${getDocumentResponseForTestNotifications.code}"
        )
        logs.printExtensiveTechnicalLogs(
            getDocumentResponseForTestNotifications, "getting test FCM " +
                    "notifications document"
        )

        val result: String = getDocumentResponseForTestNotifications.body("result.data")

        Assert.assertNull(
            result,
            "There is a push notification for a new message in the muted group chat"
        )

        onTestPassed("noPushInMutedDmChatVerification")
    }

    @Test(dependsOnMethods = ["noPushInMutedDmChatVerification"])
    fun pushesKeepComingAfterUnmutingGroupChatVerification() {

        startTest("pushesKeepComingAfterUnmutingGroupChatVerification")

        val setUserFcmTokenResponse = onboarding.setUserFcmToken(
            secondAppUserJwtToken,
            Credentials.FCM_TOKEN
        )

        Assert.assertTrue(
            setUserFcmTokenResponse.code == 200,
            "Incorrect status code for setting second user's FCM token: ${setUserFcmTokenResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(setUserFcmTokenResponse, "setting second user's FCM token")

        val feedDocID = secondUserId + "_" + chatID

        val responseForUnmutingGroupChat = home.unmuteFeedItem(
            secondAppUserJwtToken,
            feedDocID
        )

        Assert.assertTrue(
            responseForUnmutingGroupChat.code == 200,
            "Wrong status code for unmuting group chat: ${responseForUnmutingGroupChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnmutingGroupChat, "unmuting group chat")

        val responseForCreatingComment = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            commentTitle,
            chatID
        )

        Assert.assertTrue(
            responseForCreatingComment.code == 200,
            "Wrong status code for creating a comment: ${responseForCreatingComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingComment, "creating a comment")

        Thread.sleep(6000)
        val getDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_TEST_FCM_NOTIFICATIONS,
            secondUserId
        )

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Wrong status code for getting the document: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting the document")

        val document: HashMap<*, *> = getDocumentResponse.body("result.data")

        Assert.assertTrue(
            document.size != 0,
            "User didn't receive push notification"
        )

        onTestPassed("pushesKeepComingAfterUnmutingGroupChatVerification")
    }

    @Test(dependsOnMethods = ["pushesKeepComingAfterUnmutingGroupChatVerification"])
    fun pushesKeepComingAfterUnmutingDmChatVerification() {

        startTest("pushesKeepComingAfterUnmutingDmChatVerification")

        val dmFeedID = secondUserId + "_" + dmChatID

        val responseForUnmutingDmChat = home.unmuteFeedItem(
            secondAppUserJwtToken,
            dmFeedID
        )

        Assert.assertTrue(
            responseForUnmutingDmChat.code == 200,
            "Wrong status code for unmuting DM chat: ${responseForUnmutingDmChat.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnmutingDmChat, "unmuting DM chat")

        val responseForCreatingDmComment = comments.createDM(
            firstAppUserJwtToken,
            firstUserName,
            secondUserName,
            "moderate",
            dmChatID
        )

        Assert.assertTrue(
            responseForCreatingDmComment.code == 200,
            "Wrong status code for creating a DM comment: ${responseForCreatingDmComment.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForCreatingDmComment, "creating a DM comment")

        lastCreatedDmID = responseForCreatingDmComment.body("result.data.id")

        val getDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_TEST_FCM_NOTIFICATIONS,
            secondUserId
        )

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Wrong status code for getting the document: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting the document")

        val document: HashMap<*, *> = getDocumentResponse.body("result.data")

        Assert.assertTrue(
            document.size != 0,
            "User didn't receive push notification"
        )

        onTestPassed("pushesKeepComingAfterUnmutingDmChatVerification")
    }

    @Test(dependsOnMethods = ["pushesKeepComingAfterUnmutingDmChatVerification"])
    fun deleteComment() {

        startTest("deleteComment")

        val creatingCommentResponse = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            commentTitle,
            chatID
        )

        Assert.assertTrue(
            creatingCommentResponse.code == 200,
            "Wrong status code while SMS sending: ${creatingCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(creatingCommentResponse, "creating a comment")

        val commentID: String = creatingCommentResponse.body("result.data.id")

        val deleteCommentResponse = comments.deleteComment(
            firstAppUserJwtToken,
            commentID
        )

        Assert.assertTrue(
            deleteCommentResponse.code == 200,
            "Wrong status code while SMS sending: ${deleteCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(deleteCommentResponse, "deleting the comment")

        val result: Boolean = deleteCommentResponse.body("result.data.success")

        Assert.assertTrue(
            result,
            "There are no deleted comment with ID = $commentID"
        )

        onTestPassed("deleteComment")
    }

    @Test(dependsOnMethods = ["deleteComment"])
    fun blockUser() {

        startTest("blockUser")

        val responseForBlockingUser = chatID?.let { id ->
            comments.blockUser(
                firstAppUserJwtToken,
                id,
                secondUserName,
                "blockUser"
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForBlockingUser.code == 200,
            "Wrong status code for blocking user: ${responseForBlockingUser.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForBlockingUser, "blocking user")

        val result: Boolean = responseForBlockingUser.body("result.data.success")

        Assert.assertTrue(
            result,
            "The user wasn't blocked"
        )
        println("The user '$secondUserId' has been successfully blocked!")

        onTestPassed("blockUser")
    }

    @Test(dependsOnMethods = ["blockUser"])
    fun unblockUser() {

        startTest("unblockUser")

        val responseForUnblockingUser = chatID?.let { id ->
            comments.unblockUser(
                firstAppUserJwtToken,
                id,
                secondUserName
            )
        } ?: throw NoCreatedCommentsException()

        Assert.assertTrue(
            responseForUnblockingUser.code == 200,
            "Wrong status code for blocking user: ${responseForUnblockingUser.code}"
        )
        logs.printExtensiveTechnicalLogs(responseForUnblockingUser, "blocking user")

        val result: Boolean = responseForUnblockingUser.body("result.data.success")

        Assert.assertTrue(
            result,
            "The user wasn't unblocked"
        )
        println("The user '$secondUserId' has been successfully unblocked!")

        onTestPassed("unblockUser")
    }

    @Test(dependsOnMethods = ["unblockUser"])
    fun rejectComment() {

        startTest("rejectComment")

        val creatingCommentResponse = comments.createComment(
            firstAppUserJwtToken,
            firstUserName,
            "Moderate to reject",
            chatID
        )

        Assert.assertTrue(
            creatingCommentResponse.code == 200,
            "The status code for creating a comment is wrong: ${creatingCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(creatingCommentResponse, "creating a comment")

        val commentID: String = creatingCommentResponse.body("result.data.id")

        Thread.sleep(3000)
        val rejectCommentResponse = externalIntegration.rejectContent(
            supervisorJwtToken,
            commentID,
            MessageTypes.COMMENT,
            ModerationDecisionType.REJECTED,
            "Some note",
            "Other",
            ModerationRejectionSeverity.LOW
        )

        Assert.assertTrue(
            rejectCommentResponse.code == 200,
            "Wrong status code while setting contacts: ${rejectCommentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(rejectCommentResponse, "rejecting a comment")

        val getDocumentResponse = documents.getDocument(
            ServerValues.COLLECTION_NAME_COMMENTS,
            commentID
        )

        Assert.assertTrue(
            getDocumentResponse.code == 200,
            "Wrong status code for getting document: ${getDocumentResponse.code}"
        )
        logs.printExtensiveTechnicalLogs(getDocumentResponse, "getting comment document")

        val stateOfCommentInDB: String = getDocumentResponse.body("result.data.state")

        Assert.assertTrue(
            stateOfCommentInDB == "rejected",
            "Wrong comment's state in DB: $stateOfCommentInDB"
        )
        println("Comment with id '$commentID' was successfully rejected")

        onTestPassed("rejectComment")
    }

    open fun startTest(testName: String) = println("---------- Starting '$testName' test ----------")
    open fun onTestPassed(testName: String) = println("---------- Test '$testName' is passed! ----------")

    open class NoCreatedCommentsException : Exception("No comments are created yet")
}
