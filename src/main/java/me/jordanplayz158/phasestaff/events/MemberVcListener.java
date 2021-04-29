package me.jordanplayz158.phasestaff.events;

import me.jordanplayz158.phasestaff.PhaseStaff;
import me.jordanplayz158.phasestaff.json.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberVcListener extends ListenerAdapter {
    private Config config = PhaseStaff.getInstance().getConfig();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        PhaseStaff.logger.debug("Entered Listener - join");

        Guild guild = event.getGuild();
        Member member = event.getMember();
        Role role = guild.getRoleById(config.getVcRoleId());
        if (!member.getRoles().contains(role)) {
            assert role != null;
            guild.addRoleToMember(member.getId(), role).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        PhaseStaff.logger.debug("Entered Listener - leave");

        Guild guild = event.getGuild();
        Member member = event.getMember();
        Role role = guild.getRoleById(config.getVcRoleId());
        if(member.getRoles().contains(role)) {
            assert role != null;
            guild.removeRoleFromMember(member.getId(), role).queue();
        }
    }
}
