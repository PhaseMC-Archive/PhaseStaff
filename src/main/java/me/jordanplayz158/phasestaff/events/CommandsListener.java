package me.jordanplayz158.phasestaff.events;

import me.jordanplayz158.phasestaff.CommandHandler;
import me.jordanplayz158.phasestaff.PhaseStaff;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // If Prefix of Bot then continue, else do nothing
        if(event.getMessage().getContentRaw().startsWith(PhaseStaff.getInstance().getConfig().getPrefix())) {
            CommandHandler.handler(event);
        }
    }
}
