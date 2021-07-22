package flyNow.slack

import flyNow.utils.DOWNLOAD_URL
import flyNow.utils.getDate
import flyNow.utils.getLastCommit
import flyNow.utils.sendSlackMessageRequest
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class SlackNotifierTask : DefaultTask() {

    @get:Input
    abstract val channelWebhook: Property<String>

    @get:Input
    abstract val useCustomMessage: Property<Boolean>

    @get:Input
    abstract val customMessage: Property<String>

    @get:Input
    abstract val authorName: Property<String>

    @get:Input
    abstract val botUserName: Property<String>


    init {
        channelWebhook.convention("Channel webhook default value")
        customMessage.convention("Custom Message")
    }

    @TaskAction
    fun notifySlackChannel() {
        sendSlackMessageRequest(channelWebhook.get(), getSlackMessage())
    }

    private fun getSlackMessage(): String {

        val text = if (useCustomMessage.get().not()) getLastCommit() else customMessage.get()

        return """ 
                |{
                |"type": "mrkdwn",
                |"text": " *${getDate(forTxtFile = true)}* <${DOWNLOAD_URL} |Download>
                |*Changes* : $text  
                |*Author* : ${authorName.get()}\n",
                |"username": "${botUserName.get()}",
                |"icon_emoji": ":rocket:"
                |}
        """.trimMargin()
    }
}