package flyNow.utils

import org.gradle.api.provider.Property

interface FlyNow {
    val clientSecret: Property<String>
    val clientId: Property<String>
    val botName: Property<String>
    val useCustomMessage: Property<Boolean>
    val customMessage: Property<String>
    val channelWebhook: Property<String>
    val authorName: Property<String>
}
