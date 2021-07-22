package flyNow.utils

import org.gradle.api.provider.Property

abstract class FlyNow {
    abstract val clientSecret: Property<String>
    abstract val clientId: Property<String>
    abstract val botName: Property<String>
    abstract val useCustomMessage: Property<Boolean>
    abstract val customMessage: Property<String>
    abstract val channelWebhook: Property<String>
    abstract val authorName: Property<String>
}
