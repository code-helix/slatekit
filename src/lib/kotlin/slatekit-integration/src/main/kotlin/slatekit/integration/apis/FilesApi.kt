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

package slatekit.integration.apis

import slatekit.apis.Api
import slatekit.apis.Action
import slatekit.apis.AuthModes
import slatekit.apis.Verbs
import slatekit.common.Sources
import slatekit.common.types.Doc
import slatekit.common.crypto.Encryptor
import slatekit.common.io.Uris
import slatekit.common.log.Logger
import slatekit.context.Context
import slatekit.core.files.CloudFiles
import slatekit.results.Failure
import slatekit.results.Success
import slatekit.results.Try
import slatekit.results.getOrElse

@Api(area = "cloud", name = "files", desc = "api info about the application and host",
        auth = AuthModes.KEYED, roles = ["admin"], verb = Verbs.AUTO, sources = [Sources.ALL])
class FilesApi(val files: CloudFiles, override val context: Context) : slatekit.apis.support.FileSupport {

    override val encryptor: Encryptor? = context.enc
    override val logger: Logger? = context.logs.getLogger()

    @Action(desc = "creates the root folder/bucket")
    suspend fun createRootFolder(rootFolder: String):Try<String> {
        return files.createRootFolder(rootFolder)
    }

    @Action(desc = "creates a file with the supplied folder name, file name, and content")
    suspend fun create(folder: String, name: String, content: String) {
        files.create(folder, name, content)
    }

    @Action(desc = "creates a file with the supplied folder name, file name, and content from file path")
    suspend fun createFromPath(folder: String, name: String, filePath: String): Try<String> {
        return files.createFromPath(folder, name, Uris.interpret(filePath) ?: filePath)
    }

    @Action(desc = "creates a file with the supplied folder name, file name, and content from doc")
    suspend fun createFromDoc(folder: String, name: String, doc: Doc): Try<String> {
        return files.create(folder, name, doc.content)
    }

    @Action(desc = "updates a file with the supplied folder name, file name, and content")
    suspend fun update(folder: String, name: String, content: String): Try<String> {
        return files.update(folder, name, content)
    }

    @Action(desc = "updates a file with the supplied folder name, file name, and content from file path")
    suspend fun updateFromPath(folder: String, name: String, filePath: String): Try<String> {
        return files.updateFromPath(folder, name, interpretUri(filePath) ?: filePath)
    }

    @Action(desc = "updates a file with the supplied folder name, file name, and content from doc")
    suspend fun updateFromDoc(folder: String, name: String, doc: Doc): Try<String> {
        return files.updateFromPath(folder, name, doc.content)
    }

    @Action(desc = "deletes a file with the supplied folder name, file name")
    suspend fun delete(folder: String, name: String): Try<String> {
        return files.delete(folder, name)
    }

    @Action(desc = "get file as text")
    suspend fun getAsText(folder: String, name: String):Try<String> {
        return files.getAsText(folder, name)
    }

    @Action(desc = "downloads the file specified by folder and name to the local folder specified.")
    suspend fun download(folder: String, name: String, localFolder: String, display: Boolean): Try<String> {
        return show(files.download(folder, name, interpretUri(localFolder) ?: localFolder), display)
    }

    @Action(desc = "downloads the file specified by folder and name, as text content to file supplied")
    suspend fun downloadToFile(folder: String, name: String, filePath: String, display: Boolean): Try<String> {
        return show(files.downloadToFile(folder, name, Uris.interpret(filePath) ?: filePath), display)
    }

    private fun show(result: Try<String>, display: Boolean): Try<String> {
        val path = result.getOrElse { "" }
        val output = if (display) {
            val text = java.io.File(path).readText()
            "PATH   : " + path + slatekit.common.newline +
                    "CONTENT: " + text
        } else
            "PATH   : " + path
        return result.fold( { Success(output) }, { Failure(result.desc) }).toTry()
    }
}
