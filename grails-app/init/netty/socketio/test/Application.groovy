package netty.socketio.test

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {

        Thread.start {
            final def server = DashboardUpdateServer.createServer()

            try {
                server.start()
                while (true) {
                    println "processing..."
                    try {
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException ignored) {
                        println "interrupted.."
                        break;
                    }
                }
            }
            finally {
                server.stop()
            }
        }

        GrailsApp.run(Application, args)
    }
}