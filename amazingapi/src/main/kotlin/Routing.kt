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
        route("/amazingApi") {
            // should return every song
            get {
                val baseMusicFolder = File("/home/nodariko/Music/(2007) Fear Of A Blank Planet")
                val baseFileList: Array<File> = (baseMusicFolder.listFiles())?: return@get call.respond(
                    HttpStatusCode.NotFound
                ) 
                val everySong: MutableList<File> = mutableListOf()
                for (file: File in baseFileList) {
                    if(file.absolutePath.endsWith(".mp3")) {
                        everySong.add(file)
                    }
                }
                call.respondFile(everySong[0])
            } 
        }
    }
}
