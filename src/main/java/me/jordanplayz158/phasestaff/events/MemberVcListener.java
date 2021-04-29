package me.jordanplayz158.phasestaff.events;

import me.jordanplayz158.phasestaff.PhaseStaff;
import me.jordanplayz158.phasestaff.json.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberVcListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        System.out.println("Worked :)");
        super.onGuildVoiceJoin(event);
        Member member = event.getMember();
        Long id = PhaseStaff.getInstance().getConfig().getVcRoleId();
        Role role = member.getGuild().getRoleById(id);
        if (!member.getRoles().contains(role)) {
            member.getRoles().add(role);

        }

    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        System.out.println("Worked :-)");

        super.onGuildVoiceLeave(event);
        Member member = event.getMember();
        Long id = PhaseStaff.getInstance().getConfig().getVcRoleId();
        Role role = member.getGuild().getRoleById(id);
        if (member.getRoles().contains(role)) {
            member.getRoles().remove(role);

        }
    }
}
