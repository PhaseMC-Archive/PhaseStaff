package me.jordanplayz158.phasestaff.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ModerationCommand extends ArgumentCommand {
    public ModerationCommand(@NotNull String name, List<String> aliases, @NotNull String description, Permission permission, Role role, @NotNull String syntax) {
        super(name, aliases, description, permission, role, syntax);
    }
}
