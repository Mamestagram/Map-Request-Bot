package org.example.Event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.Embed;
import org.example.Main;
import org.example.Object.Bot;
import org.example.Object.Database;
import org.example.Object.Modal;
import org.example.Object.Setting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateRequest extends ListenerAdapter {

    private static Matcher getRegexMap(String url) {

        String pattern = "https://osu.ppy.sh/beatmapsets/(\\d+)#(\\w+)/(\\d+)";
        Pattern regex = Pattern.compile(pattern);

        return regex.matcher(url);
    }

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

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {
        if(e.getModalId().equals("modal_ranked_mapset") || e.getModalId().equals("modal_unranked_mapset") || e.getModalId().equals("modal_ranked_map") || e.getModalId().equals("modal_unranked_map")) {

            String set_id = null, mode = null, id = null;
            String link = e.getValue("bmap_url").getAsString();
            Matcher matcher = getRegexMap(link);
            Database db = Main.database;

            //送信の処理を書く
            if (matcher.find()) {
                try {
                    Connection connection = db.getConnection(db.getDB_HOST(), db.getDB_NAME(), db.getDB_USER(), db.getDB_PASSWORD());
                    set_id = matcher.group(1);
                    mode = matcher.group(2);
                    id = matcher.group(3);

                } catch (SQLException ex) {
                    e.replyEmbeds(Embed.getMapRequestError().build()).setEphemeral(true).queue();
                }
            }
        }
    }
}
