package netty.socketio.test

import org.apache.commons.lang3.tuple.ImmutablePair
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener

class DashboardMessageListener implements MessageListener {

    @Override
    void onMessage(Message message) {
        def messageBody = slurper.parseText(new String(message.body))

        def item = new DashboardUpdateService.ReceiptUpdateItem()
        item.rfi = messageBody.rfi
        item.time = new Date((long) messageBody.completedTimestamp)

        def event
        if (message.messageProperties.type.equalsIgnoreCase("com.carfax.receipt.common.rabbitmq.FileRouterMessage")) {
            item.file = messageBody.originalFilepath
            event = "fileQueued"
        } else { // log message
            item.file = messageBody.filepath

            switch (item.completedStep) {
                case 'receipt_started':
                    event = "fileProcessing"
                    break

                case 'receipt_completed':
                    event = "fileCompleted"
                    break

                default:
                    return
            }
        }
        dashboardUpdates.add(new ImmutablePair<String, DashboardUpdateService.ReceiptUpdateItem>(event, item))
    }
}
