package me.jordanplayz158.phasestaff.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ArgumentCommand extends Command {
    public ArgumentCommand(@NotNull String name, List<String> aliases, @NotNull String description, Permission permission, Role role, @NotNull String syntax) {
        super(name, aliases, description, permission, role, syntax);
    }

    /*public void logAction(MessageReceivedEvent event, User user, String actionAdjective, String reason) {
        long logChannelId = PhaseStaff.getInstance().getConfig().getChannelLog();

        if(logChannelId == 0) {
            event.getChannel().sendMessage(PhaseStaff.getInstance().getTemplate(event.getAuthor())
                    .setColor(Color.RED)
                    .setTitle("Log Channel is not set!")
                    .setDescription("Please set it in the config!")
                    .build()).queue();

            return;
        }

        // Makes sure that no matter what guild the log channel belongs to, it will send the message to the log channel.
        for(Guild guild : event.getJDA().getGuilds()) {
            try {
                EmbedBuilder embed = PhaseStaff.getInstance().getTemplate(event.getAuthor())
                        .setColor(Color.GREEN)
                        .setTitle(MessageUtils.upperCaseFirstLetter(name) + " Successful");

                if(user == null) {
                    embed.setDescription(event.getAuthor().getId() + " was " + actionAdjective + " by " + MessageUtils.boldNameAndTag(event.getAuthor()));
                } else {
                    embed.setDescription(MessageUtils.boldNameAndTag(user) + " was " + actionAdjective + " by " + MessageUtils.boldNameAndTag(event.getAuthor()));
                }

                if(reason != null) {
                    embed.addField("Reason", reason, false);
                }

                Objects.requireNonNull(guild.getTextChannelById(logChannelId))
                        .sendMessage(embed.build()).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}

