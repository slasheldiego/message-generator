package messages.generator.azure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.List;

import com.azure.messaging.eventhubs.*;
import com.azure.core.amqp.AmqpTransportType;

import messages.generator.entities.Event;
import messages.generator.utils.Utils;
import messages.generator.interfaces.Sender;

public class AzureEventHubSender extends Thread implements Sender{
    
    private String connectionString = "";
    private String eventHubName = "";
    private int n = -1;

    public AzureEventHubSender(int n) {
        this.LoadConfig();
        this.n = n;
        System.out.println("Este es la clase sender del hilo: " + this.n);
    }

    String message() {
        return "Este es un mensaje de prueba ... " + connectionString;
    }

    public void sendMessages() {
        // create a producer using the namespace connection string and event hub name
        EventHubProducerClient producer = new EventHubClientBuilder().transportType(AmqpTransportType.AMQP_WEB_SOCKETS)
                .connectionString(connectionString, eventHubName).buildProducerClient();

        List<Event> list = Utils.generateEvents(10);

        postEventEHubBatch(producer, list);
        
    }

    public void postEventEHubBatch(EventHubProducerClient producer, List<Event> messages) {
        // prepare a batch of events to send to the event hub
        EventDataBatch batch = producer.createBatch();

        for (Event eventJson : messages) {

            batch.tryAdd(new EventData(eventJson.toString()));

        }

        producer.send(batch);
    
        // close the producer
        producer.close();
    }

    public void LoadConfig() {
        String pathConf = this.getClass().getResource("/config/config.properties").toString().replace("file:","").replace("%20"," ");
        try (InputStream input = new FileInputStream(pathConf)) {

            Properties prop = new Properties();
            prop.load(input);
            connectionString = prop.getProperty("connectionString");
            eventHubName = prop.getProperty("eventHubName");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void printEvents(int lenght, List<Event> events) {
        for (int i = 0; i < lenght; i++) {
            System.out.println(events.get(i).getId() + " " + events.get(i).getDate() + " " + events.get(i).getQuantity()
                    + " " + events.get(i).getUnitPrice() + " " + events.get(i).getCountry() + " "
                    + events.get(i).getDescription());
        }
    }

    public void run() {
        while (true) {
            this.sendMessages();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}