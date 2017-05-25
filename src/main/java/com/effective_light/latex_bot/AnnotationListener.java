package com.effective_light.latex_bot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class AnnotationListener
{
    private IDiscordClient client;

    AnnotationListener(IDiscordClient client)
    {
        this.client = client;
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event)
    {
        IUser ourUser = event.getClient().getOurUser();
        System.out.println( ourUser.getStringID() );
        System.out.printf( "Logged in as %s!", ourUser.getName() );
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String content = event.getMessage().getContent();

        String cmd = "!tex ";

        if ( content.startsWith( cmd ) )
        {
            String tex = content.substring( cmd.length() ).trim();
            
        }

    }
}
