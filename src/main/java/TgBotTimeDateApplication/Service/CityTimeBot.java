package TgBotTimeDateApplication.Service;
import TgBotTimeDateApplication.Config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CityTimeBot extends TelegramLongPollingBot {

   private   final BotConfig  config;


    public CityTimeBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "write city name"));
        listofCommands.add(new BotCommand("/help", "help"));

        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.getMessage();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String city = update.getMessage().getText();

            switch (city){
                case "/start":
                   sendMessage(update.getMessage().getChatId(),"Write City name");
                   break;
                case "/help":
                    break;

            }

            try {


                ZoneId zoneId = ZoneId.of(city);
                ZonedDateTime time = ZonedDateTime.now(zoneId);
                String response = city + ": " + time.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z"));
                sendMessage(update.getMessage().getChatId(), response);

            } catch (Exception e) {
                sendMessage(update.getMessage().getChatId(), "Invalid city. Please enter a valid city name.");
            }
        }
    }

    private void sendMessage(long chatId, String response) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
