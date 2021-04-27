package me.jordanplayz158.phasestaff.commands;

import me.jordanplayz158.phasestaff.PhaseStaff;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public abstract class Command {
    protected String name;
    protected List<String> aliases;
    protected String description;
    protected Permission permission;
    protected final Role role;
    protected String syntax;

    public Command(@NotNull String name, List<String> aliases, @NotNull String description, Permission permission, Role role, @NotNull String syntax) {
        this.name = name;

        if(aliases != null) {
            this.aliases = aliases;
        }

        this.description = description;
        this.permission = permission;
        this.role = role;
        this.syntax = syntax;
    }

    public abstract void onCommand(MessageReceivedEvent event, String[] args);

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public Permission getPermission() {
        return permission;
    }

    public Role getRole() {
        return role;
    }

    public String getSyntax() {
        return syntax;
    }

    public boolean noReason(MessageReceivedEvent event, String[] args) {
        if (args.length < 3) {
            event.getChannel().sendMessage(PhaseStaff.getInstance().getTemplate(event.getAuthor())
                    .setColor(Color.RED)
                    .setTitle("Invalid Reason!")
                    .setDescription("Reason cannot be null")
                    .build()).queue();

            return true;
        }

        return false;
    }
}