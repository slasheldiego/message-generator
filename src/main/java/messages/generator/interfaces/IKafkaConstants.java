package messages.generator.interfaces;

public interface IKafkaConstants {
    public static String KAFKA_BROKERS = "z-2.amskuse1edlbqas001.sbknrs.c3.kafka.us-east-1.amazonaws.com:2181,z-3.amskuse1edlbqas001.sbknrs.c3.kafka.us-east-1.amazonaws.com:2181,z-1.amskuse1edlbqas001.sbknrs.c3.kafka.us-east-1.amazonaws.com:2181";

    public static Integer MESSAGE_COUNT=1000;

    public static String CLIENT_ID="client1";

    public static String TOPIC_NAME="AWSKafkaTutorialTopic";

    public static String GROUP_ID_CONFIG="consumerGroup1";

    public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;

    public static String OFFSET_RESET_LATEST="latest";

    public static String OFFSET_RESET_EARLIER="earliest";

    public static Integer MAX_POLL_RECORDS=1;
}
