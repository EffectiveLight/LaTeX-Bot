package com.effective_light.latex_bot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BotMain
{

    public static void main(String[] args) throws IOException
    {
        Properties settings = new Properties();
        File propFile = new File( "settings.properties" );

        if ( !propFile.exists() )
        {
            settings.setProperty( "token", "token_plz" );
            settings.store( new FileOutputStream( propFile ), "LaTeX-Bot Setting(s)" );
            return;
        }

        settings.load( new FileInputStream( propFile ) );

        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken( settings.getProperty( "token" ) );
        IDiscordClient client = clientBuilder.login();

        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener( new AnnotationListener( client ) );
    }
}
