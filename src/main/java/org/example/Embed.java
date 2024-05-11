package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public abstract class Embed {

    public static EmbedBuilder getMapRequestMessage() {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Map Status Change Request**");
        embed.addField(":pencil: How to request", "Simply enter the requested information!", false);
        embed.addField(":white_check_mark: Changeable status", "<:ranked:1143570271974989914> Ranked\n:x: Unranked", true);
        embed.addField(":warning: Note", "Maps that have not been played cannot be requested", true);
        embed.setImage("https://cdn.discordapp.com/attachments/944984741826932767/1238116305224204309/image.png");
        embed.setColor(Color.MAGENTA);
        return embed;
    }

    public static EmbedBuilder getMapRequestConfirmMessage() {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Check your request**");
        embed.addField(":pencil: In which style would you like to request the map?", "<:ranked:1143570271974989914> Mapset\n<:ranked:1143570271974989914> Map", false);
        embed.setImage("https://cdn.discordapp.com/attachments/944984741826932767/1238116305224204309/image.png");
        embed.setColor(Color.MAGENTA);

        return embed;
    }

    public static EmbedBuilder getMapRequestCompleteMessage(String message) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.addField(":white_check_mark: Complete", message, false);
        embed.setImage("https://cdn.discordapp.com/attachments/944984741826932767/1238116305224204309/image.png");
        embed.setColor(Color.MAGENTA);

        return embed;
    }


    public static EmbedBuilder getMapRequestErrorMessage(String error) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.addField(":warning: Error", error, false);
        embed.setImage("https://cdn.discordapp.com/attachments/944984741826932767/1238116305224204309/image.png");
        embed.setColor(Color.MAGENTA);

        return embed;
    }

    public static EmbedBuilder getMapRequestReceivedMessage(User user, int set_id, String comment, String filename, String status, String type, String mode) {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle( "**["+ status + "] " + mode + " " + type + " request received!!**");
            embed.addField("**Filename**", filename, false);
            embed.addField("**Comment**", "```" + comment + "```", true);
            embed.addField("**Request**", user.getAsMention(), true);
            embed.setImage("https://assets.ppy.sh/beatmaps/" + set_id + "/covers/cover.jpg?");

            if(status.equals("ranked")) {
                embed.setColor(Color.GREEN);
            } else {
                embed.setColor(Color.RED);
            }
            return embed;
    }
}
