package com.effective_light.latex_bot;

import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        System.out.printf( "Logged in as %s (%s)!\n",
                ourUser.getName(), ourUser.getStringID() );
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws IOException
    {
        String content = event.getMessage().getContent();

        String cmd = "!tex ";

        if ( content.startsWith( cmd ) )
        {
            String tex = content.substring( cmd.length() ).trim();

            System.out.printf( "[%s (%s) -> LaTeX] %s\n",
                    event.getAuthor().getName(), event.getAuthor().getStringID(), tex );
            try
            {
                TeXFormula formula = new TeXFormula( tex );

                BufferedImage image = ( BufferedImage ) formula
                        .createBufferedImage( TeXConstants.STYLE_DISPLAY, settings.getImageSize(),
                                settings.getTextColor(), settings.getBackgroundColor() );

                try ( ByteArrayOutputStream outputStream = new ByteArrayOutputStream() )
                {
                    ImageIO.write( image, "png", outputStream );

                    try ( InputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray() ) )
                    {

                        new MessageBuilder( client ).withChannel( event.getChannel() )
                                .withFile( inputStream, "tex.png" ).build();
                    }
                }
            } catch ( ParseException e )
            {
                new MessageBuilder( client ).withChannel( event.getChannel() ).withContent(
                                String.format( "**The given input was unable to be rendered!**\n**Reason:** ```Markdown\n%s```",
                                e.getMessage() ) ).build();
            }
        }
    }
}
