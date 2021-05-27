package org.tuomola.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nats.client.Connection;
import io.nats.client.Nats;

public class NatsSender {
  private Connection nc;
  private ObjectMapper mapper = new ObjectMapper();
  
  public void sendTodo(TodoObject todo) throws IOException, InterruptedException
  {
    if(nc == null) 
      connect();

    nc.publish("todo", mapper.writeValueAsString(todo).getBytes(StandardCharsets.UTF_8));
  }

  private void connect() throws IOException, InterruptedException
  {
    nc = Nats.connect("nats://my-nats.default:4222");
  }
}
