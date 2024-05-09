package org.example;

import net.dv8tion.jda.api.EmbedBuilder;

public abstract class Embed {

    public static EmbedBuilder getMapRequestMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Map Request");

        return embed;
    }
}
