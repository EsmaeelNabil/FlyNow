package flyNow.utils

var DOWNLOAD_URL = "https://www.google.com"

const val apkPath = "/outputs/apk/debug/app-debug.apk"
const val PLUGIN_NAME = "flyNow"
const val EXTENSION_NAME = "flyNow"
const val BOT_NAME = "flyNow"
val CONFIG_MESSAGE = """
    |
    |Please Configure the Plugin with : values cann't be empty!!
    |
    |flyNow {
    |         clientId         = Google Drive API client id 
    |         clientSecret     = Google Drive API client secret
    |         
    |         channelWebhook   = SLACK_CHANNEL_WEBHOOK
    |         
    |         /* 
    |         * default = False
    |         * if used pass a message throw the terminal like this
    |         * ./gradlew $PLUGIN_NAME -Pmessage="Your Custom Slack Message"
    |         */
    |         useCustomMessage = true or false 
    |      }
    |      
    """.trimMargin()


const val NOT_GIT_PROJECT_MESSAGE = "Make sure this is a Git repository first!"
const val CLIENT_ID_EMPTY_MESSAGE = "ClientId Can't be empty"
const val SLACK_WEBHOOK_EMPTY_MESSAGE = "slack `channelWebhook` Can't be empty"
const val CLIENT_SECRET_EMPTY_MESSAGE = "ClientSecret Can't be empty"
const val MESSAGE_TERMINAL_PARAM = "message"
const val ASSEMBLE_DEBUG_TASK = "assembleDebug"

val CUSTOM_MESSAGE_EXCEPTION_MESSAGE = """
    | flyNow plugin configured with 
    | useCustomMessage = true 
    | but you didn't use it like this : ./gradlew -Pmessage="Slack Custom Message" 
""".trimMargin()