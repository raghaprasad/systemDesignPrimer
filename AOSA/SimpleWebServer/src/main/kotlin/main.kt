import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.File
import java.io.OutputStream
import java.net.InetSocketAddress

/*
 1. Wait for someone to connect to our server and send an HTTP request;
 2. parse that request;
 3.figure out what it's asking for;
 4. fetch that data (or generate it dynamically);
 5. format the data as HTML; and
 6. send it back.
 7. TODO : Build CGI
 For #1 through #3 leverage Java's HttpServer
 */
interface Case {
    fun test(fullPath: String): Boolean
    fun act(callback: (content: String, statusCode: Int) -> Unit)
}

class CaseNoFile : Case {
    private lateinit var fileFullPath: String
    override fun test(fullPath: String): Boolean {
        fileFullPath = fullPath
        val f = File(fullPath)
        return !f.exists()
    }

    override fun act(callback: (content: String, statusCode: Int) -> Unit) {
        callback(
            """
                <html>
                <body>
                <p>$fileFullPath not found</p>
                </body>
                </html>
            """.trimIndent(), 404
        )
    }
}

class CaseExistingFile : Case {
    private lateinit var f: File
    override fun test(fullPath: String): Boolean {
        f = File(fullPath)
        return f.exists() && f.isFile
    }

    override fun act(callback: (content: String, statusCode: Int) -> Unit) {
        callback(f.readText(Charsets.UTF_8), 200)
    }
}

class CaseDirectoryIndexFile : Case {
    private lateinit var f: File
    override fun test(fullPath: String): Boolean {
        f = File(fullPath)
        return f.isDirectory
    }

    override fun act(callback: (content: String, statusCode: Int) -> Unit) {
        val content = """
            <html>
            <body>
            <p> You are in a directory </p>
            <p>${f.listFiles().map { f: File -> f.path.toString() }}</p>
            </body>
            </html>
            """.trimIndent()
        println(content)
        callback(content, 200)
    }
}

class RequestHandler : HttpHandler {
    private fun HttpExchange.sendPage(page: String, statusCode: Int = 200) {
        this.sendResponseHeaders(statusCode, page.length.toLong())
        var outputStream: OutputStream = this.responseBody
        outputStream.write(page.toByteArray())
        outputStream.close()
    }

    private fun HttpExchange.getRelativePath(): String {
        return this.requestURI.toString().replaceFirst(this.httpContext.path, "")
    }

    override fun handle(exchange: HttpExchange) {
        println("Handling incoming request to serve " + exchange.requestURI)
        val fileToServe = exchange.getRelativePath()
        val currentUserDir = System.getProperty("user.dir")
        val fileName = """${currentUserDir}/content/${fileToServe}"""
        for (case in listOf<Case>(CaseExistingFile(), CaseDirectoryIndexFile(), CaseNoFile())) {
            if (case.test(fileName)) {
                case.act { content, statusCode -> exchange.sendPage(content, statusCode) }
                break
            }
        }
    }
}

fun main() {
    println("Starting simple web server")
    var server: HttpServer = HttpServer.create(InetSocketAddress(8000), 0)
    server.createContext("/test", RequestHandler())
    server.executor = null
    server.start()
}