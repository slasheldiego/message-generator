package messages.generator.interfaces;

public interface IKafkaConstants {
    public static String KAFKA_BROKERS = "<your cluster brokers>";

    public static Integer MESSAGE_COUNT=1000;

    public static String CLIENT_ID="client1";

    public static String TOPIC_NAME="AWSKafkaTutorialTopic";

    public static String GROUP_ID_CONFIG="consumerGroup1";

    public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;

    public static String OFFSET_RESET_LATEST="latest";

    public static String OFFSET_RESET_EARLIER="earliest";

    public static Integer MAX_POLL_RECORDS=1;
}
