package org.example.Event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.Embed;
import org.example.Main;
import org.example.Object.Bot;
import org.example.Object.Modal;
import org.example.Object.Setting;

public class CreateRequest extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        Setting setting = Main.setting;
        Bot bot = Main.bot;

        JDA jda = bot.getJda();

        if (e.getMessage().getChannel().getIdLong() == setting.getREQUEST_CHANNEL_ID()) {
            if (e.getMessage().getContentRaw().equals("create-req-msg")) {
                jda.getGuildById(e.getGuild().getIdLong()).getTextChannelById(e.getChannel().getIdLong()).sendMessageEmbeds(Embed.getMapRequestMessage().build())
                        .addActionRow(
                                Button.success("btn_ranked", "Ranked"),
                                Button.danger("btn_unranked", "Unranked")
                        ).queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        // フォームの種類を分けるため、処理分け
        if (e.getComponentId().equals("btn_ranked")) {
            e.replyEmbeds(Embed.getMapRequestConfirmMessage().build())
                    .addActionRow(
                            Button.success("btn_ranked_mapset", "Mapset"),
                            Button.primary("btn_ranked_map", "Map")
            ).setEphemeral(true).queue();
        }
        else if (e.getComponentId().equals("btn_unranked")) {
            e.replyEmbeds(Embed.getMapRequestConfirmMessage().build())
                    .addActionRow(
                            Button.success("btn_unranked_mapset", "Mapset"),
                            Button.primary("btn_unranked_map", "Map")
                    ).setEphemeral(true).queue();
        }
        // WIP
        else if (e.getComponentId().equals("btn_ranked_mapset")) {
            e.replyModal(Modal.getMapRequestModal("modal_ranked_mapset")).queue();
        }
        else if (e.getComponentId().equals("btn_ranked_map")) {
            e.replyModal(Modal.getMapRequestModal("modal_ranked_map")).queue();
        }
        else if (e.getComponentId().equals("btn_unranked_mapset")) {
            e.replyModal(Modal.getUnRankMapRequestModal("modal_unranked_mapset")).queue();
        }
        else if (e.getComponentId().equals("btn_unranked_map")) {
            e.replyModal(Modal.getUnRankMapRequestModal("modal_unranked_map")).queue();
        }
    }
}
