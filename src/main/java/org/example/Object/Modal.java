package org.example.Object;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public abstract class Modal {

    public static TextInput createTextInput(String id, String label, String description, boolean isRequire, TextInputStyle style) {

        return TextInput.create(id, label, style)
                .setMinLength(1)
                .setPlaceholder(description)
                .setRequired(isRequire)
                .build();
    }

    public static net.dv8tion.jda.api.interactions.modals.Modal getMapRequestModal(String custom_id) {

        TextInput map_id = org.example.Object.Modal.createTextInput("bmap_url", "Beatmap URL", "eg: https://osu.ppy.sh/beatmapsets/1#osu/1", true, TextInputStyle.SHORT);
        TextInput comment = org.example.Object.Modal.createTextInput("comment", "Comment", "Enter the comment", false, TextInputStyle.PARAGRAPH);

        return net.dv8tion.jda.api.interactions.modals.Modal.create(custom_id, "Beatmap Ranked Request")
                .addActionRows(
                        ActionRow.of(map_id),
                        ActionRow.of(comment)
                ).build();
    }

    public static net.dv8tion.jda.api.interactions.modals.Modal getUnRankMapRequestModal(String custom_id) {

        TextInput map_id = org.example.Object.Modal.createTextInput("bmap_url", "Beatmap URL", "eg: https://osu.ppy.sh/beatmapsets/1#osu/1", true, TextInputStyle.SHORT);
        TextInput comment = org.example.Object.Modal.createTextInput("comment", "Reason", "Enter the reason", true, TextInputStyle.PARAGRAPH);

        return net.dv8tion.jda.api.interactions.modals.Modal.create(custom_id, "Beatmap Unranked Request")
                .addActionRows(
                        ActionRow.of(map_id),
                        ActionRow.of(comment)
                )
                .build();
    }
}
