package flyNow.utils

import org.gradle.api.Project
import org.gradle.api.Task
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.api.services.drive.model.File as DriveFile

fun String.trimForFileName() = this.replace(" ", "_").replace(":", "_").trim()

fun getDate(forTxtFile: Boolean = false): String {
    val current = LocalDateTime.now()
    val formatter = if (forTxtFile) {
        DateTimeFormatter.ofPattern("dd MMM, yyy hh:mm:ss")
    } else {
        DateTimeFormatter.ofPattern("MMM-dd hh")
    }
    return current.format(formatter).trim()
}

fun Project.getBranchName(): String {
    val branchName = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
        standardOutput = branchName
    }
    return branchName.toString().getBranchName
}

fun Task.getBranchName() = project.getBranchName()

fun Project.getLastCommit(): String {
    val commit = ByteArrayOutputStream()
    exec {
        commandLine("git", "log", "--oneline")
        standardOutput = commit
    }

    return commit.toString().getCommitMessage.lines().first()
}

fun Task.getLastCommit() = project.getLastCommit()

fun Task.updateName(apkFile: File): File {
    val local = apkFile

    val branchName = "${getBranchName()}".trim()

    val newApk = File("${project.debugPath}/${branchName}.apk")

    local.absoluteFile.renameTo(newApk)

    if (local.exists())
        println("Apk name Changed to : $branchName.apk")
    else
        println("Apk name didn't change as required!! something went wrong")

    return local
}

fun sendSlackMessageRequest(path: String, requestParams: String) {

    val url = URL(path)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
    con.requestMethod = "POST"
    con.setRequestProperty("Content-Type", "application/json; utf-8")
    con.setRequestProperty("Accept", "application/json");
    con.doOutput = true


    con.outputStream.use { os ->
        val input = requestParams.toByteArray(charset("utf-8"))
        os.write(input, 0, input.size)
    }

    BufferedReader(
        InputStreamReader(con.inputStream, "utf-8")
    ).use { br ->
        val response = StringBuilder()
        var responseLine: String?
        while (br.readLine().also { responseLine = it } != null) {
            response.append(responseLine!!.trim { it <= ' ' })
        }

        if (response.toString() == "ok")
            println("Slack Notified Successfully")
        else
            println(response.toString())
    }
}


fun DriveFile.getDriveLink() = "https://drive.google.com/open?id=${this.id}"
private val String.getBranchName get() = this.replace("/", "-").trim()
private val String.getCommitMessage get() = this.drop(8).trim()

private val apkPath = "/outputs/apk/debug/app-debug.apk"


val Project.debugApkFile get() = File("${project.buildDir.absoluteFile}$apkPath")
val Project.debugPath get() = "${project.buildDir.absoluteFile}/outputs/apk/debug"

//val Project.generatedApkFile get() = File("${project.buildDir.absoluteFile}/outputs/apk/debug/${getBranchName()}.apk")
val Project.generatedApk get() = "${project.buildDir.absoluteFile}/outputs/apk/debug/${getBranchName()}.apk"

val Project.buildDir: File get() = this.buildDir.absoluteFile
val Task.buildDir: File get() = project.buildDir

var DOWNLOAD_URL = "htttps://www.google.com"
val EXTENSION_NAME = "flyNow"