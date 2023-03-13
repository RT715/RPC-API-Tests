import org.json.JSONObject

class JsonCreation {

    fun getJwtTokenRequestBody(userId: String): String {

        JSONObject().also {
            it.put(ServerValues.FIELD_UID, userId)
            it.put(ServerValues.FIELD_PASSWORD, Credentials.PASSWORD)

            return it.toString()
        }
    }

    fun createBodyString(
        method: String,
        params: Map<String, Any?>,
        //decisionParams: Map<String, Any?>? = null
    ): String {

        JSONObject().also {
            it.put("jsonrpc", "2.0")
            it.put("id", "1")
            it.put("method", method)

            val paramsJSON = JSONObject()

            params.keys.forEach { key -> paramsJSON.put(key, params[key] ?: JSONObject.NULL) }

            it.put("params", paramsJSON)

//            decisionParams?.let { params ->
//                //val decisions = JSONArray().
//                val decision = JSONObject()
//                decision.put(ServerValues.FIELD_CONTENT_ID, params[ServerValues.FIELD_CONTENT_ID])
//                decision.put(ServerValues.FIELD_CONTENT_TYPE, params[ServerValues.FIELD_CONTENT_TYPE])
//                decision.put(ServerValues.FIELD_DECISION, params[ServerValues.FIELD_DECISION])
//                decision.put(ServerValues.FIELD_NOTES, params[ServerValues.FIELD_NOTES])
//                decision.put(
//                    ServerValues.FIELD_REJECTION_REASON,
//                    params[ServerValues.FIELD_REJECTION_REASON] ?: JSONObject.NULL
//                )
//                decision.put(
//                    ServerValues.FIELD_REJECTION_SEVERITY,
//                    params[ServerValues.FIELD_REJECTION_SEVERITY] ?: JSONObject.NULL
//                )
//
//                println(params)
//                println(decision)
//                //decisions.put(decision)
//                paramsJSON.put(ServerValues.FIELD_DECISION, decision)
//            }
//            println(it)
            return it.toString()
        }
    }
}