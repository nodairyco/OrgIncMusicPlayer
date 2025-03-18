package org.inc

import com.sun.source.tree.NewArrayTree
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import java.io.File
import kotlin.io.path.fileVisitor

fun Application.configureRouting() {
    routing {
        route("/api/songs") {
            // should return every song
            get {
                val baseMusicFolder = File("/Users/mr.mgeli/Desktop/(2007) Fear Of A Blank Planet")
                val baseFileList: Array<File> = (baseMusicFolder.listFiles())?: return@get call.respond(
                    HttpStatusCode.NotFound
                )

                val songNames = baseFileList
                        .filter{ it.absolutePath.endsWith(".mp3") }
                        .map{ it.nameWithoutExtension }
                val htmlContent = """
                    <h1>Music Library</h1>
                    <ul>
                        ${songNames.joinToString("") { songName ->
                    "<li><a href='/api/songs/$songName'>$songName</a></li>"
                }}
                    </ul>
                """.trimIndent()

                call.respondText(htmlContent, ContentType.Text.Html)
            }
        }

        route("/api/songs/{songName}") {
            get {
                val songName = call.parameters["songName"]?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Missing song name"
                )

                val baseMusicFolder = File("/Users/mr.mgeli/Desktop/(2007) Fear Of A Blank Planet")
                val baseFileList: Array<File> = (baseMusicFolder.listFiles())?: return@get call.respond(
                    HttpStatusCode.NotFound
                )

                val songFile = baseFileList
                        .filter{ it.absolutePath.endsWith(".mp3") }
                        .find{ it.nameWithoutExtension == songName }
                        ?: return@get call.respond(
                            HttpStatusCode.NotFound, "Song not found"
                        )

                val htmlContent = """
                    <h1>Now Playing: $songName</h1>
                    <audio controls>
                        <source src="/api/songs/$songName/file" type="audio/mpeg">
                        Your browser does not support the audio element.
                    </audio>
                    <p><a href="/api/songs">Back to Music Library</a></p>
                """.trimIndent()

                call.respondText(htmlContent, ContentType.Text.Html)
            }

            get("api/songs/{songName}/file") {
                val songName = call.parameters["songName"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest, "Song name is required"
                )

                val baseMusicFolder = File("/Users/mr.mgeli/Desktop/(2007) Fear Of A Blank Planet")
                val songFile = File(baseMusicFolder, "$songName.mp3")

                if (!songFile.exists()) {
                    return@get call.respond(HttpStatusCode.NotFound, "Song not found")
                }

                call.response.header(HttpHeaders.ContentType, ContentType.Audio.MPEG.toString())
                call.respondFile(songFile)
            }

        }
    }
}
