package org.example.Object;

import io.github.cdimascio.dotenv.Dotenv;

public class Setting {

    private long GUILD_ID;
    private long REQUEST_CHANNEL_ID;
    private long TESTER_CHANNEL_ID;

    public Setting () {
        Dotenv dotenv = Dotenv.configure()
                .load();
        GUILD_ID = Long.parseLong(dotenv.get("GUILD_ID"));
        REQUEST_CHANNEL_ID = Long.parseLong(dotenv.get("REQ_CHANNEL_ID"));
        TESTER_CHANNEL_ID = Long.parseLong(dotenv.get("TESTER_CHANNEL_ID"));
    }

    public long getREQUEST_CHANNEL_ID() {
        return REQUEST_CHANNEL_ID;
    }

    public long getTESTER_CHANNEL_ID() {
        return TESTER_CHANNEL_ID;
    }

    public long getGUILD_ID() {
        return GUILD_ID;
    }
}
