package me.jordanplayz158.phasestaff.events;

import me.jordanplayz158.phasestaff.PhaseStaff;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class MemberJoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().addRoleToMember(
                event.getMember(),
                Objects.requireNonNull(
                        event.getGuild().getRoleById(
                                PhaseStaff.getInstance().getConfig().getRoleOnJoin()))).queue();
    }
}
