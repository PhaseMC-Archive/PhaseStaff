package me.jordanplayz158.phasestaff;

import me.jordanplayz158.phasestaff.commands.ArgumentCommand;
import me.jordanplayz158.phasestaff.commands.Command;
import me.jordanplayz158.phasestaff.commands.ModerationCommand;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandHandler {
    private static List<Command> commands = new ArrayList<>();

    public static void handler(MessageReceivedEvent event) {
        // Removes the prefix (substring) then splits the message into arguments by spaces
        String[] args = event.getMessage().getContentRaw().substring(PhaseStaff.getInstance().getConfig().getPrefix().length()).split("\\s+");
        String commandName = args[0];

        Command command = null;

        for(Command c : commands) {
            if (c.getName().equals(commandName) || (c.getAliases() != null && c.getAliases().contains(commandName))) {
                command = c;
                break;
            }
        }

        // Did it find a registered command from the commandName?
        if (command == null) {
            return;
        }

        MessageChannel channel = event.getChannel();

        if ((command.getPermission() != null && !Objects.requireNonNull(event.getMember()).hasPermission(command.getPermission())) || (command.getRole() != null && !Objects.requireNonNull(event.getMember()).getRoles().contains(command.getRole()))) {
            channel.sendMessage(PhaseStaff.getTemplate(event.getAuthor()).setColor(Color.RED).setTitle("Access Denied").setDescription("You do not have access to that command!").build()).queue();
            return;
        }

        boolean isArgumentCommand = command.getClass().getSuperclass() == ArgumentCommand.class;
        boolean isModerationCommand = command.getClass().getSuperclass() == ModerationCommand.class;

        if ((isArgumentCommand || isModerationCommand) && args.length == 1) {
            StringBuilder aliases = new StringBuilder();

            if(command.getAliases() != null) {
                aliases.append("\n\n").append(MarkdownUtil.bold("Known Aliases")).append("\n");

                for (String alias : command.getAliases()) {
                    aliases.append(PhaseStaff.getInstance().getConfig().getPrefix()).append(alias).append("\n");
                }
            }

            channel.sendMessage(PhaseStaff.getTemplate(event.getAuthor()).setColor(Color.YELLOW)
                    .setTitle(command.getName())
                    .setDescription(command.getDescription() + aliases)
                    .addField("Syntax", PhaseStaff.getInstance().getConfig().getPrefix() + command.getSyntax(), true).build()).queue();
            return;
        }

        if(isModerationCommand) {
            Guild guild = event.getGuild();
            User mentionedUser = MessageUtils.extractMention(args[1]);

            if (guild.isMember(mentionedUser)) {
                if (!hierarchyCheck(guild.getRoles(), Objects.requireNonNull(event.getMember()).getRoles(), Objects.requireNonNull(guild.getMember(mentionedUser)).getRoles(), channel)) {
                    channel.sendMessage(PhaseStaff.getTemplate(event.getAuthor()).setColor(Color.RED).setTitle("Hierarchy Check").setDescription("You can't execute this command as you are lower or the same on the hierarchy than the person you are using this on.").build()).queue();
                    return;
                }
            }
        }

        command.onCommand(event, args);
    }

    private static boolean hierarchyCheck(List<Role> guildRoles, List<Role> memberRoles, List<Role> mentionRoles, MessageChannel channel) {
        /*channel.sendMessage("Member's Highest Role: "
                + guildRoles.indexOf(memberRoles.get(0))
                + "\nMention's Highest Role: "
                + guildRoles.indexOf(mentionRoles.get(0))
                + "\nIs the member's highest role higher than the mentioned member's highest role? "
                + (guildRoles.indexOf(memberRoles.get(0)) < guildRoles.indexOf(mentionRoles.get(0)))).queue();*/

        if(memberRoles.size() < 1) {
            return false;
        }

        if(mentionRoles.size() < 1) {
            return true;
        }

        return guildRoles.indexOf(memberRoles.get(0)) < guildRoles.indexOf(mentionRoles.get(0));
    }

    public static void addCommands(Command... commands) {
        CommandHandler.commands.addAll(Arrays.asList(commands));
    }

    public static void removeCommands(Command... commands) {
        CommandHandler.commands.removeAll(Arrays.asList(commands));
    }
}
