package me.jordanplayz158.phasestaff.commands;

import me.jordanplayz158.phasestaff.PhaseStaff;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class UserInfoCommand extends ArgumentCommand {
    public UserInfoCommand() {
        super("userinfo",
                null,
                "See the information of a user in the discord.",
                null,
                null,
                "userinfo <mention/id>");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        try {
            Member member = event.getGuild().getMemberById(MessageUtils.extractMention(args[1]).getId());
            event.getChannel().sendMessage(PhaseStaff.getTemplate(event.getAuthor())
                    .setTitle(MessageUtils.nameAndTag(member.getUser()))
                    .addField("Join Time", String.valueOf(member.getTimeJoined()), true)
                    .setColor(Color.GREEN)

                    .build()
            ).queue();
        } catch (NullPointerException e) {
            event.getChannel().sendMessage(PhaseStaff.getTemplate(event.getAuthor())
                    .setTitle("Error | User not found")
                    .setDescription("Is the user in the guild?")
                    .setColor(Color.RED)
                    .build()
            ).queue();
        }
    }
}
