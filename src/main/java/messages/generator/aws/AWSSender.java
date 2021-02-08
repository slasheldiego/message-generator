package messages.generator.aws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;

import messages.generator.utils.Utils;


public class AWSSender extends Thread{

    private Utils utils = new Utils();
    private String arn = "";
    private int n = -1;
    private InstanceProfileCredentialsProvider credentials =
                InstanceProfileCredentialsProvider.createAsyncRefreshingProvider(true);
    private SnsClient snsClient;

    public AWSSender(int n){
        System.out.println("Este es la clase sender ...");
        this.LoadConfig();
        this.n = n;
        
        this.snsClient = SnsClient.builder()
                .region(Region.US_EAST_1)
                .build();
        
    }

    public void sendMessages(){
        List<String> list = utils.list2JsonList(utils.generateEvents(1));

        for (String event: list){
            pubTopic(this.snsClient,event,arn);
        }
    }

    public static void pubTopic(SnsClient snsClient, String message, String topicArn) {

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
}