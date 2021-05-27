package org.tuomola.project;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class MyTelegramClient {
  private Long chat_id = 610105091L; 
  private TelegramBot bot = null;


  public MyTelegramClient()
  {
  }

  public void sendMessage(String messageText) {
    if(chat_id == null)
      return;

    SendMessage message = new SendMessage(chat_id, messageText);
    bot.execute(message);
  }
 
  public String getBotToken() {
    return System.getenv("BROADCASTER_APIKEY");
  }

  public void startClient() {
    // Create your bot passing the token received from @BotFather
    bot = new TelegramBot(getBotToken());
  }

}
