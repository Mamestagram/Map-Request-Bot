package org.example.Event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NominateRequest extends ListenerAdapter {

    private static Matcher getRegexMap(String url) {

        String pattern = "https://osu.ppy.sh/beatmapsets/(\\d+)#(\\w+)/(\\d+)";
        Pattern regex = Pattern.compile(pattern);

        return regex.matcher(url);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        Setting setting = Main.setting;
        Database database = Main.database;
        Bot bot = Main.bot;

        JDA jda = bot.getJda();

        if (e.getMessage().getChannel().getIdLong() == setting.getTESTER_CHANNEL_ID()) {

            if (e.getMember().getUser().isBot()) {

                if (e.getMessage().isEphemeral()) {
                    return;
                }

                if (!e.getMessage().getEmbeds().isEmpty()) {
                    return;
                }

                try {

                    PreparedStatement ps;
                    ResultSet result;
                    Connection connection = database.getConnection(database.getDB_HOST(), database.getDB_NAME(), database.getDB_USER(), database.getDB_PASSWORD());

                    String[] args = e.getMessage().getContentRaw().split(",");

                    System.out.println(args.length);
                    int[] arr = new int[args.length];

                    int count;
                    String m;
                    String status;
                    String type;
                    String comment;

                    if (args.length == 5) {
                        count = 1;
                        m = args[1];
                        status = args[2];
                        type = args[3];
                        comment = args[4];
                    } else {
                        count = 2;
                        m = args[2];
                        status = args[3];
                        type = args[4];
                        comment = args[5];
                    }

                    for (int i = 0; i < count; i++) {
                        arr[i] = Integer.parseInt(args[i]);
                    }

                    ps = connection.prepareStatement("select set_id, artist, title, creator from maps where set_id = ? limit 1");
                    ps.setInt(1, arr[0]);
                    result = ps.executeQuery();
                    if (result.next()) {
                        String filename = result.getString("artist") + " - " + result.getString("title") + " by " + result.getString("creator");
                        e.getMessage().createThreadChannel("[" + status  + "] "  + filename)
                                .queue(thread -> thread.sendMessage( "https://osu.ppy.sh/beatmapsets/" + arr[0] + "#" + m + "/" + arr[1])
                                        .setActionRow(
                                                Button.success("btn_map_accept", "Accept"),
                                                Button.danger("btn_map_reject", "Reject")
                                        ).queue());
                        e.getMessage().replyEmbeds(Embed.getMapRequestReceivedMessage(result.getInt("set_id"), comment, filename, status, type, m).build()).queue();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }

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

            int set_id = 1, id = 1;
            String type = "osu,mapset";
            String link = e.getValue("bmap_url").getAsString();

            Matcher matcher = getRegexMap(link);
            Bot bot = Main.bot;
            Database db = Main.database;
            Setting setting = Main.setting;

            PreparedStatement ps;
            ResultSet result;

            JDA jda = bot.getJda();

            //送信の処理を書く
            if (matcher.find()) {
                try {
                    Connection connection = db.getConnection(db.getDB_HOST(), db.getDB_NAME(), db.getDB_USER(), db.getDB_PASSWORD());

                    set_id = Integer.parseInt(matcher.group(1));
                    id = Integer.parseInt(matcher.group(3));

                    if (e.getModalId().contains("unranked")) {
                        type = matcher.group(2) + ",unranked";
                    } else {
                        type = matcher.group(2) + ",ranked";
                    }
                    if(set_id < 0 || id < 0) {
                        e.replyEmbeds(Embed.getMapRequestErrorMessage("Incorrect setID or ID format").build()).setEphemeral(true).queue();
                        return;
                    }

                    ps = connection.prepareStatement("SELECT id FROM maps where set_id = ? limit 1");
                    ps.setInt(1, set_id);
                    result = ps.executeQuery();
                    if (result.next()) {

                        String comment;

                        if(e.getValue("comment").getAsString().equals("")) {
                                comment = "No comment";
                        } else {
                            comment = e.getValue("comment").getAsString();
                        }

                        if (e.getModalId().contains("mapset")) {
                            jda.getGuildById(setting.getGUILD_ID()).getTextChannelById(setting.getTESTER_CHANNEL_ID()).sendMessage(set_id + ","  + type + ",mapset," + comment).queue();
                        } else {
                            jda.getGuildById(setting.getGUILD_ID()).getTextChannelById(setting.getTESTER_CHANNEL_ID()).sendMessage(set_id + "," + id + "," + type + ",map," + comment).queue();
                        }
                        e.replyEmbeds(Embed.getMapRequestCompleteMessage("Your request has been sent to the nominator.").build()).setEphemeral(true).queue();
                    } else {
                        e.replyEmbeds(Embed.getMapRequestErrorMessage("The entered map does not exist in the database!").build()).setEphemeral(true).queue();
                    }
                } catch (SQLException ex) {
                    e.replyEmbeds(Embed.getMapRequestErrorMessage("An unexpected error has occurred. Please send your request again.\n\nError: SQLException").build()).setEphemeral(true).queue();
                }
            } else {
                e.replyEmbeds(Embed.getMapRequestErrorMessage("Incorrect URL format").build()).setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {
        if (e.getChannelType().isThread()) {
            if (e.getChannel().asThreadChannel().getOwner().getUser().isBot()) {

            }
        }
    }
}
