/**
 * <slate_header>
 * url: www.slatekit.com
 * git: www.github.com/code-helix/slatekit
 * org: www.codehelix.co
 * author: Kishore Reddy
 * copyright: 2016 CodeHelix Solutions Inc.
 * license: refer to website and/or github
 * about: A tool-kit, utility library and server-backend
 * mantra: Simplicity above all else
 * </slate_header>
 */

package slatekit.core.push

import slatekit.common.*
import slatekit.common.conf.Conf
import slatekit.common.log.Logs
import slatekit.results.Failure
import slatekit.results.Notice
import slatekit.results.Success

/**
 * Google FCM ( Fire base Cloud Messaging ) Service.
 * Provides a simple interface to call FCM for push notifications.
 *
 * SEE: https://firebase.google.com/docs/cloud-messaging/android/receive
 *
 *
 * URLS:
 * 1. https://fcm.googleapis.com/fcm/send      ( for modern FCM http requests )
 * 2. https://gcm-http.googleapis.com/gcm/send ( for older  GCM http requests )
 *    GCM will be deprecated on April, 2019

 *
 * MESSAGE TYPES:
 *
 *  1. Notification message
 *   {
 *       "to": "{{REG_ID_HERE}}",
 *       "notification": {
 *           "click_action" : ".MainActivity",
 *           "title" : "title v1",
 *           "text": "data v1!",
 *           "icon": "ic_launcher"
 *       }
 *   }
 *
 *   2. Data Message
 *   {
 *       "to": "{{REG_ID_HERE}}",
 *       "data": {
 *           "title" : "title v1",
 *           "text": "data v1!",
 *           "icon": "ic_launcher"
 *       }
 *   }
 *
 *   3. Notification And Data message
 *   {
 *       "to": "{{REG_ID_HERE}}",
 *       "notification": {
 *           "click_action" : ".MainActivity",
 *           "title" : "title v1",
 *           "text": "data v1!",
 *           "icon": "ic_launcher"
 *       },
 *       "data": {
 *           "title" : "title v1",
 *           "text": "data v1!",
 *           "icon": "ic_launcher"
 *       }
 *   }
 *
 *  NOTES:
 *  1. https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase/44150822#44150822
 *  2. https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase/42279260#42279260
 */
open class MessageServiceGoogle(
        _key: String,
        val config: Conf,
        val logs: Logs
) :
    MessageServiceBase() {

    private val settings = MessageSettings("", _key, "")
    private val baseUrl = config.getStringOrElse("android.sendUrl", fcmUrl)
    private val sendNotifications = config.getBoolOrElse("android.sendNotifications", true)
    private val logger = logs.getLogger(this.javaClass)

    /**
     * Sends a push notification to Android using the data from the Message supplied.
     */
    override fun send(msg: Message): Notice<Boolean> {
        return if (sendNotifications) {
            sendSync(msg)
        } else {
            logger.warn("Push notification disabled for: ${msg.to}")
            Success(true, msg = "Disabled")
        }
    }

    /**
     * Sends the message asynchronously
     *
     * @param msg : message to send
     * @return
     * @note : implement in derived class that can actually send the message
     */
    override fun sendAsync(msg: Message, callback:(Notice<Boolean>) -> Unit) {

    }

    /**
     * Builds the Message to send as Push notification as an immutable HTTP Request
     * when the app is either in the background/killed, it must
     * have a notification object along w/ the data object.
     *
     * See links:
     * https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase/44150822#44150822
     * https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase/42279260#42279260
     * https://firebase.google.com/docs/cloud-messaging/android/receive
     */
    protected fun sendSync(msg: Message):Notice<Boolean> {

        // 1. Build "to" field
        // This correctly based on if sending to multiple devices
        // 1  = "to" : "regid1"
        // 2+ = "registration_ids" : ["regid1", "regid2" ]
        val ids = msg.to.joinToString(",") { "\"" + it + "\"" }
        val to = if (msg.isMultiDelivery) "\"registration_ids\"" else "\"to\""
        val recipient = if (msg.isMultiDelivery) "[$ids]" else ids
        val alert = msg.alert?.let { buildAlert(it) } ?: ""

        // 2. Build the content
        // This depends on if your sending a "notification" | "data" message or both.
        // Notifications only showup in the notification area on android.
        // Data messages will be handled in the app.
        // Use both for when an app is closed/backgrounded.
        val content = when (msg.messageType) {
            is MessageTypeData -> "{$to:$recipient, \"data\":${msg.payload}}"
            is MessageTypeAlert -> "{$to:$recipient, \"notification\":$alert}"
            is MessageTypeBoth -> "{$to:$recipient, \"notification\":$alert, \"data\":${msg.payload}}"
            else -> "{$to:$recipient, \"notification\":$alert}"
        }

        // 3. Send off w/ json
        val result = HttpRPC().sendSync(
                method = HttpRPC.Method.Post,
                url = baseUrl,
                headers = mapOf(
                        "Content-Type" to "application/json",
                        "Authorization" to "key=" + settings.key
                ),
                body = HttpRPC.Body.JsonContent(content)
        )
        return result.fold( { Success(true) }, { Failure(it.message ?: "") })
    }

    private fun buildAlert(alert: Notification): String {
        return """{
            "click_action" : "${alert.click_action}",
            "title" : "${alert.title.replace("\"", "\\\"")}",
            "text": "${alert.text.replace("\"", "\\\"")}",
            "icon": "${alert.icon.replace("\"", "\\\"")}"
        }"""
    }

    companion object {

        @JvmStatic
        val fcmUrl = "https://fcm.googleapis.com/fcm/send"
    }
}

/*
byte[] bytes = body.getBytes(UTF8);
HttpURLConnection conn = getConnection(url);
conn.setDoOutput(true);
conn.setUseCaches(false);
conn.setFixedLengthStreamingMode(bytes.length);
conn.setRequestMethod("POST");
conn.setRequestProperty("Content-Type", "application/json");
conn.setRequestProperty("Authorization", "key=" + key);
OutputStream out = conn.getOutputStream();
*/
