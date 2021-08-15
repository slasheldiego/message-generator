package messages.generator.aws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;

import messages.generator.interfaces.Sender;
import messages.generator.utils.Utils;
import messages.generator.entities.Event;


public class AWSSNSSender extends Thread implements Sender{

    private String arn = "";
    private InstanceProfileCredentialsProvider credentials =
                InstanceProfileCredentialsProvider.createAsyncRefreshingProvider(true);

    public AWSSNSSender(int n){
        System.out.println("Este es la clase sender ...");
        this.LoadConfig();
        
    }

    public void sendMessages(){

        SnsClient snsClient = SnsClient.builder()
        .region(Region.US_EAST_1)
        .build();

        List<Event> list = Utils.generateEvents(5);

        for (Event event: list){
            postEventSNS(snsClient,event.toString(),arn);
        }
    }

    public void postEventSNS(SnsClient snsClient, String message, String topicArn) {

        try {
            PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();
    
            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
    
         } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
              System.exit(1);
         }
    }

    public void LoadConfig() {
        String pathConf = this.getClass().getResource("/config/config.properties").toString().replace("file:","").replace("%20"," ");
        try (InputStream input = new FileInputStream(pathConf)) {
            Properties prop = new Properties();
            prop.load(input);
            arn = prop.getProperty("aws.arn");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void CloseSyncCredentials(){
        try{
            credentials.close();
        }catch(IOException ex){
            ex.printStackTrace();
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