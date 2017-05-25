package com.effective_light.latex_bot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

import java.io.IOException;

public class BotMain
{

    public static void main(String[] args) throws IOException
    {
        Settings settings = new Settings();

        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken( settings.getToken() );
        IDiscordClient client = clientBuilder.login();

        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener( new EventListener( client, settings ) );
    }
}
