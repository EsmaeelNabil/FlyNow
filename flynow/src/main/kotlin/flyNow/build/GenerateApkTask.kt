package flyNow.build

import flyNow.utils.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateApkTask : DefaultTask() {

    companion object {
        const val name = "generateApk"
    }

    @TaskAction
    fun generate() {

        val apkFile = project.debugApkFile

        logger.info("Apk generated : ${apkFile.absolutePath}")


        val oldPath = "$buildDir/outputs/apk/debug/app-debug.apk"

        val newApkName = "${getDate().trimForFileName()}-${getBranchName()}.apk"

        val renamedApkPath = "$buildDir/outputs/apk/debug/$newApkName"


        val newPath = File(oldPath)

        if (newPath.exists()) {
            logger.error("debug new path : $renamedApkPath")

            val renamed = newPath.renameTo(File(renamedApkPath))
            if (renamed)
                logger.info("from : app-debug.apk to : ------> $newApkName")
            else logger.error("COULDN'T RENAME from : app-debug.apk to : ------> $newApkName \n make sure old file with same name doesn't exist")
        } else {
            println("Apk does not exist! , assembleDebug first!")
        }

    }
}