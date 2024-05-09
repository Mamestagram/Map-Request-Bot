package org.example.Request;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.example.Object.Setting;

public class CreateRequest extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        Setting setting = Main.setting;

        if (e.getMessage().getChannel().getIdLong() == setting.getREQUEST_CHANNEL_ID()) {
            if (e.getMessage().getContentRaw().equals("create-req-msg")) {

            }
        }
    }
}
