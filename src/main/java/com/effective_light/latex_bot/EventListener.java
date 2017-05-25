package com.effective_light.latex_bot;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EventListener
{
    private IDiscordClient client;
    private Settings settings;

    EventListener(IDiscordClient client, Settings settings)
    {
        this.client = client;
        this.settings = settings;
    }

    @EventSubscriber
    public void onReady(ReadyEvent event)
    {
        IUser ourUser = event.getClient().getOurUser();
        System.out.println( ourUser.getStringID() );
        System.out.printf( "Logged in as %s!\n", ourUser.getName() );
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws IOException
    {
        String content = event.getMessage().getContent();

        String cmd = "!tex ";

        if ( content.startsWith( cmd ) )
        {
            String tex = content.substring( cmd.length() ).trim();
            TeXFormula formula = new TeXFormula( tex );
            BufferedImage image = ( BufferedImage ) formula
                    .createBufferedImage( TeXConstants.STYLE_DISPLAY, 20,
                            new Color( 219, 219, 219 ), new Color( 54, 57, 62 ) );

            try ( ByteArrayOutputStream outputStream = new ByteArrayOutputStream() )
            {
                ImageIO.write( image, "png", outputStream );

                try ( ByteArrayInputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray() ) )
                {
                    new MessageBuilder( client ).withChannel( event.getChannel() )
                            .withFile( inputStream, "tex.png" ).build();
                }

            }

        }

    }
}
