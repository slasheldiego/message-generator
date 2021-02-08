package messages.generator.interfaces;

import software.amazon.awssdk.services.sns.SnsClient;

public interface Sender {
    public void sendMessages();
}

