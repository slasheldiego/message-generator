package messages.generator.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;

import messages.generator.interfaces.IKafkaConstants;

public class ProducerCreator {
    public static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, IKafkaConstants.SECURITY_PROTOCOL_CONFIG);
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, IKafkaConstants.SSL_TRUSTSTORE_LOCATION_CONFIG);
        return new KafkaProducer<>(props);
    }

    
}
