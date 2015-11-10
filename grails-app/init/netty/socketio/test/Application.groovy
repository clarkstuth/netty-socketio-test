package netty.socketio.test

import com.rabbitmq.client.ConnectionFactory
import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.apache.commons.lang3.tuple.ImmutablePair
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.util.ErrorHandler

class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Bean
    ConnectionFactory rabbitmqConnectionFactory() {
        def connFactory = new ConnectionFactory()
        connFactory.setHost("localhost")
        connFactory.setPort(3710)
        connFactory.setUsername("receiptIntegration")
        connFactory.setPassword("receiptIntegration")
        connFactory.setAutomaticRecoveryEnabled(true)

        connFactory
    }

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory rabbitmqConnectionFactory) {

        def listener = new SimpleMessageListenerContainer(new CachingConnectionFactory(rabbitmqConnectionFactory))
        listener.setQueueNames("alpha.receipt.dashboard")
        listener.setPrefetchCount(1)
        listener.setConcurrentConsumers(1)
        listener.setAcknowledgeMode(AcknowledgeMode.AUTO)

        listener.setErrorHandler(new ErrorHandler() {
            @Override
            void handleError(Throwable t) {
                t.printStackTrace()
            }
        })

        listener
    }

}