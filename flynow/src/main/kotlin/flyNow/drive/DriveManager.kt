package flyNow.drive

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.batch.json.JsonBatchCallback
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonError
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.http.FileContent
import com.google.api.client.http.HttpHeaders
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.Permission
import flyNow.utils.PLUGIN_NAME
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess
import com.google.api.services.drive.model.File as DriveFile


class DriveManager {

    /** Application name.  */
    private val APPLICATION_NAME = PLUGIN_NAME

    /** Directory to store user credentials for this application.  */
    private val DATA_STORE_DIR = File(
        System.getProperty("user.home"), ".credentials/${PLUGIN_NAME}"
    )

    /** Global instance of the [FileDataStoreFactory].  */
    private var DATA_STORE_FACTORY: FileDataStoreFactory? = null

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /** Global instance of the HTTP transport.  */
    private var HTTP_TRANSPORT: HttpTransport? = null

    /** Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private val SCOPES = listOf(DriveScopes.DRIVE)

    init {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
            DATA_STORE_FACTORY = FileDataStoreFactory(DATA_STORE_DIR)
        } catch (t: Throwable) {
            t.printStackTrace()
            exitProcess(1)
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun authorize(): Credential {

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, client_id, client_secret, SCOPES
        )
            .setDataStoreFactory(DATA_STORE_FACTORY)
            .setAccessType("offline")
            .build()

        val credential = AuthorizationCodeInstalledApp(
            flow, LocalServerReceiver.Builder().setPort(8888).build()
        ).authorize("drive-user")

        println("Credentials saved to " + DATA_STORE_DIR.absolutePath)
        return credential
    }


    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * *
     * @throws IOException
     */
    val service: Drive
        @Throws(IOException::class)
        get() {
            val credential = authorize()
            return Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
        }


    fun upload(
        uploadFile: File,
        onUploadProgress: (MediaHttpUploader) -> Unit = { _ -> },
        onPermissionsUpdateSuccess: (DriveFile) -> Unit = { _ -> },
        onPermissionsUpdateError: (GoogleJsonError) -> Unit = { _ -> }
    ) {

        val driveFile = DriveFile().also {
            it.name = uploadFile.name
        }

        val uploadRequest = service.files()
            .create(driveFile, FileContent("application/octet-stream", uploadFile))

        // upload File
        val uploadedFile = with(uploadRequest) {
            mediaHttpUploader.progressListener = MediaHttpUploaderProgressListener {
                onUploadProgress(it)
            }
            execute()
        }

        // update File Permissions

        with(service.batch()) {
            // create
            service.permissions().create(
                uploadedFile.id,
                Permission().setType("anyone").setRole("reader")
            ).queue(
                this,
                object : JsonBatchCallback<Permission>() {
                    override fun onSuccess(t: Permission?, responseHeaders: HttpHeaders?) {
                        onPermissionsUpdateSuccess(uploadedFile)
                    }

                    override fun onFailure(e: GoogleJsonError?, responseHeaders: HttpHeaders?) {
                        e?.let(onPermissionsUpdateError)
                    }
                })

            // update
            execute()
        }
    }

    private var client_id = ""
    private var client_secret = ""

    fun setCredentials(clientId: String, clientSecret: String): DriveManager {
        client_id = clientId
        client_secret = clientSecret
        return this
    }


}