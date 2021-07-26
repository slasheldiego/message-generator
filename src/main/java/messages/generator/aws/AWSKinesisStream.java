package messages.generator.aws;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.KinesisClientUtil;

import messages.generator.entities.Event;
import messages.generator.interfaces.Sender;
import messages.generator.utils.Utils;

public class AWSKinesisStream extends Thread implements Sender{

    String streamName;
    String regionName;

    private static final Log LOG = LogFactory.getLog(AWSKinesisStream.class);

    public AWSKinesisStream(String streamName, String regionName, int num) {
        this.streamName = streamName;
        this.regionName = regionName;
    }

    private static void validateStream(KinesisAsyncClient kinesisClient, String streamName) {
        try {
            DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder().streamName(streamName)
                    .build();
            DescribeStreamResponse describeStreamResponse = kinesisClient.describeStream(describeStreamRequest).get();
            if (!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void sendEvent(String event, KinesisAsyncClient kinesisClient, String streamName) {
        Gson gson = new Gson();
        byte[] bytes = event.getBytes();
        // The bytes could be null if there is an issue with the JSON serialization by
        // the Jackson JSON library.
        if (bytes == null) {
            LOG.warn("Could not get JSON bytes for event");
            return;
        }

        Event evt = gson.fromJson( event, Event.class );

        LOG.info("Putting event: " + event.toString());
        PutRecordRequest request = PutRecordRequest.builder().partitionKey(evt.getCountry()) // We use the country as
                                                                                               // the partition key,
                                                                                               // explained in the
                                                                                               // Supplemental
                                                                                               // Information section
                                                                                               // below.
                .streamName(streamName).data(SdkBytes.fromByteArray(bytes)).build();
        try {
            kinesisClient.putRecord(request).get();
        } catch (InterruptedException e) {
            LOG.info("Interrupted, assuming shutdown.");
        } catch (ExecutionException e) {
            LOG.error("Exception while sending data to Kinesis. Will try again next cycle.", e);
        }
    }

    public void sendMessages() {

        Region region = Region.of(this.regionName);
        if (region == null) {
            System.err.println(this.regionName + " is not a valid AWS region.");
            System.exit(1);
        }

        KinesisAsyncClient kinesisClient = KinesisClientUtil
                .createKinesisAsyncClient(KinesisAsyncClient.builder().region(region));

        // Validate that the stream exists and is active
        validateStream(kinesisClient, this.streamName);

        List<String> events = Utils.generateEvents(1);

        sendEvent(events.get(0),kinesisClient,this.streamName);
        
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
