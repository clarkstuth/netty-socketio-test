package netty.socketio.test

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.ConnectListener
import groovy.json.JsonSlurper
import org.apache.commons.lang3.tuple.Pair
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.beans.factory.DisposableBean

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class DashboardUpdateService implements DisposableBean {

    // Grails convention to use any transactional management when interacting with this service
    def transactional = false

    SocketIOServer server
    final BlockingQueue<Pair<String, ReceiptUpdateItem>> dashboardUpdates = new ArrayBlockingQueue<>(50)
    SimpleMessageListenerContainer listener
    final JsonSlurper slurper = new JsonSlurper()

    Collection<SocketIOClient> connectedClients;

    void start() {

        def config = new Configuration()
        config.setOrigin("http://localhost:8080")
        config.setHostname("localhost")
        config.setPort(9092)

        server = new SocketIOServer(config)

        server.addConnectListener(new ConnectListener() {
            @Override
            void onConnect(SocketIOClient client) {

                sendTempUpdates(client)
//                sendUpdatesFromQueue(client)
            }

            void sendUpdatesFromQueue(SocketIOClient client) {
                def update = dashboardUpdates.take()
                client.sendEvent(update.key, update.value)
            }

            void sendTempUpdates(SocketIOClient client) {
                def max = 7

                for (num in (1..(max + 2))) {
                    def time = new Date().format("MM-dd-yyyy H:m")
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

        server.start()
    }

    @Override
    void destroy() throws Exception {
        server.stop()
    }

    class ReceiptUpdateItem {
        def rfi
        def time
        def file
    }

}