package netty.socketio.test

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.ConnectListener

class DashboardUpdateServer {

    static SocketIOServer createServer() {

        final Configuration config = new Configuration()

        config.setHostname("localhost")
        config.setPort(9092)
        config.setOrigin("http://localhost:8080");

        final SocketIOServer server = new SocketIOServer(config)

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {

                def max = 7

                for (num in (1..(max + 2))) {
                    def time = new Date().format("m-d-Y H:m")
                    def currentFile = [id: num, file: "/some/file/" + num + ".txt", time: time]

                    if (num <= max) {
                        println "sending dashboard update - File Queued - $currentFile"
                        client.sendEvent("fileQueued", currentFile)
                    }

                    if (num > 1 && num <= max + 1) {
                        def i = num - 1
                        def lastFile = [id: i, file: "/some/file/" + i + ".txt", time: time]
                        println "sending dashboard update - File Processing - $lastFile"
                        client.sendEvent("fileProcessing", lastFile)
                    }

                    if (num > 2 && num <= max + 2) {
                        def i = num - 2
                        def nextLastFile = [id: i, file: "/some/file/" + i + ".txt", time: time]
                        println "sending dashboard update - File Completed - $nextLastFile"
                        client.sendEvent("fileCompleted", nextLastFile)
                    }

                    try {
                        Thread.sleep(300)
                    } catch (InterruptedException ignored) {
                        Thread.interrupted()
                        break
                    }
                }
            }
        })

        server
    }
}