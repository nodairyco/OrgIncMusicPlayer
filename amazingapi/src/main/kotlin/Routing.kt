package org.inc

import com.sun.source.tree.NewArrayTree
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import kotlin.io.path.fileVisitor

fun func(): MutableList<File>{
    val baseMusicFolder = File("/home/nodariko/Music/(2007) Fear Of A Blank Planet")
    val baseFileList: Array<File> = (baseMusicFolder.listFiles())?: throw Error("cool error")
    val everySong: MutableList<File> = mutableListOf()
    for (file: File in baseFileList) {
        if(file.extension == "mp3") {
            everySong.add(file)
        }
    }
    return everySong
}

fun Application.configureRouting() {
    routing {
        route("/amazingApi") {
            // should return every song
            get {
               
            } 
            
            get("/{fileName}") {
                val everySong = func()
                val requiredFileName = call.request.pathVariables["fileName"]
                try {
                    val filteredList = everySong.find { 
                        AudioFileIO.read(it).tag.getFirst(FieldKey.TITLE).lowercase() == requiredFileName 
                    }?:return@get call.respond(HttpStatusCode.NotFound)
                    
                    call.respondFile(filteredList)
                } catch (e: Exception){
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
