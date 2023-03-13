import letsopen.*

open class Settings {

    private val domain = getCurrentURL()
    private val fullLog = getFullLogForTest()
    private val protocol = getProtocol()
    val supervisorID = getSupervisorId()

    private val adminUrl = "$protocol://api.admin.letsopen.$domain/json-rpc"
    private val urlForGettingToken = "$protocol://api.letsopen.$domain/debug/get-token"
    private val baseUrl = "$protocol://api.letsopen.$domain/json-rpc"

    val auth = Authentication(domain, fullLog, protocol)
    val documents = Documents(domain, fullLog, protocol)
    val onboarding = Onboarding(domain, fullLog, protocol)
    val home = Home(domain, fullLog, protocol)
    val comments = Comments(domain, fullLog, protocol)
    val externalIntegration = ExternalIntegrations(domain, fullLog, protocol)
    val suggestedFeed = SuggestedFeed(domain, fullLog, protocol)
    val logs = Log(baseUrl, adminUrl, urlForGettingToken, fullLog)

    var chatID: String? = null
    var dmChatID: String? = null
    var lastCreatedCommentID: String? = null
    var lastCreatedDmID: String? = null
    var anonToken: String? = null
    lateinit var sharedChatID: String
    lateinit var firstAppUserJwtToken: String
    lateinit var secondAppUserJwtToken: String
    lateinit var firstUserId: String
    lateinit var secondUserId: String
    lateinit var lastCreatedCommentText: String
    lateinit var lastCreatedCommentTextInDB: String
    lateinit var supervisorJwtToken: String

    val firstUserName = "kind camel"
    val secondUserName = "gentle penguin"
    val chatTitle = "Chat creation by API request №${Utilities.randomValue(5)}"
    val commentTitle = "Comment creation by API request №${Utilities.randomValue(4)}"
    val dmTitle = "DM creation by API request №${Utilities.randomValue(3)}"

    private fun getProtocol(): String {

        var protocol = System.getenv("protocol")

        if (domain == "loc")
            protocol = "http"
        else if (domain == "dev" || domain == "io")
            protocol = "https"

        return protocol
    }

    private fun getCurrentURL(): String {

        var domain = System.getenv("domain")

        println("Start on domain $domain")

        if (domain == null)
            domain = "io"
        return domain
    }

    private fun getFullLogForTest(): Boolean {

        val flag = System.getenv("fullLog")

        var returnValue = false
        if (flag != null)
            returnValue = true

        return returnValue
    }

    private fun getSupervisorId(): String {

        return if (domain == "io")
            Credentials.SUPERVISOR_ID_STAGE
        else
            Credentials.SUPERVISOR_ID_DEV
    }
}