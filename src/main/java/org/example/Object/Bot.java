package org.example.Object;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.Event.NominateRequest;

public class Bot {

    private String TOKEN;

    private JDA jda;

    public Bot() {
        Dotenv dotenv = Dotenv.configure()
                .load();

        TOKEN = dotenv.get("TOKEN");
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public JDA getJda() {
        return jda;
    }


    public void loadJDA() {
        jda = JDABuilder.createDefault(this.TOKEN)
                .setRawEventsEnabled(true)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS
                ).enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.EMOJI
                )
                .disableCache(
                        CacheFlag.VOICE_STATE,
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS
                ).setActivity(
                        Activity.playing("Supporting for all users"))
                .addEventListeners(
                        new NominateRequest()
                )
                .build();
    }
}
