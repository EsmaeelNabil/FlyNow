package flyNow.drive

import flyNow.utils.*
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File


abstract class UploadToDriveTask : DefaultTask() {

    @get:Input
    abstract val client_id: Property<String>

    @get:Input
    abstract val client_secret: Property<String>

    init {
        client_id.convention("default client_id")
        client_secret.convention("default secret")
    }

    @TaskAction
    fun uploadApk() {

        // set Credentials
        val manager = DriveManager().setCredentials(client_id.get(), client_secret.get())

        val newApkName = "${getDate().trimForFileName()}-${getBranchName()}.apk"
        val renamedApkPath = "$buildDir/outputs/apk/debug/$newApkName"

        // generated APk
        val apk = File(renamedApkPath)

        if (apk.exists())
        // upload the apk and log the progress
            manager.upload(
                apk,
                onUploadProgress = { uploader ->
                    logger.quiet(
                        "Uploaded: {} {}[bytes]({})",
                        uploader.uploadState,
                        String.format("%,3d", uploader.numBytesUploaded),
                        String.format("%2.1f%%", uploader.progress * 100)
                    )
                },
                onPermissionsUpdateSuccess = { uploadedApk ->
                    DOWNLOAD_URL = uploadedApk.getDriveLink()
                    logger.quiet("Apk ${apk.name} is uploaded Successfully ")
                    logger.quiet("Google Drive link: $DOWNLOAD_URL")
                },
                onPermissionsUpdateError = { googleJsonError ->
                    logger.error("Could not update permissions : ${googleJsonError.errors}")
                })
        else
            logger.error("generated Apk with path : ${project.generatedApk} \nisn't found!!!")
    }
}