package org.tuomola.project;

import java.io.IOException;

public class Broadcaster {

	public static void main(String[] args) throws IOException, InterruptedException {

			MyTelegramClient mtc = new MyTelegramClient();
			mtc.startClient();
			mtc.sendMessage("Starting broadcaster...");

			NatsClient nc = new NatsClient();

			nc.connectToServer();
			nc.startSubscriber(mtc);
	}
}