package org.tuomola.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;

public class NatsClient {
  private Connection nc;
  
  public void connectToServer() throws IOException, InterruptedException
  {
    nc = Nats.connect("nats://my-nats.default:4222");
  }

  public void startSubscriber(MyTelegramClient mtc)
  {
    Subscription sub = nc.subscribe("todo", "myqueue");

    while(true) {
      try {
        Message msg = sub.nextMessage(Duration.ofMillis(0));
        if(msg != null) {
            String message = new String(msg.getData(), StandardCharsets.US_ASCII);
            System.out.printf("Received %s\n", message);
            mtc.sendMessage(message);
        }
      } catch (InterruptedException e) {
          // just try again
      }        
    }
  }
}
