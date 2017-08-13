package com.effective_light.latex_bot;

import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventListener
{
    private static final String CMD = "!tex ";
    private static Pattern pattern = Pattern.compile( "`(" + CMD + ".*?)`" );

    private IDiscordClient client;
    private Settings settings;
    private Map<Long, IMessage> replies;

    EventListener(IDiscordClient client, Settings settings)
    {
        this.client = client;
        this.settings = settings;
        this.replies = new HashMap<>();
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
        renderLaTex( event.getMessage() );
    }

    @EventSubscriber
    public void onMessageUpdate(MessageUpdateEvent event) throws IOException
    {
        if ( replies.containsKey( event.getOldMessage().getLongID() ) )
        {
            IMessage message = replies.get( event.getOldMessage().getLongID() );
            if ( !message.isDeleted() )
                message.delete();

            if ( !event.getNewMessage().isDeleted() )
                renderLaTex( event.getMessage() );
        }
    }

    private void renderLaTex(IMessage message) throws IOException
    {
        String content = message.getContent();
        IMessage messageSent;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
        String creationTime = message.getTimestamp().format( formatter );
        String editTime = message.getEditedTimestamp().map(
                localDateTime -> localDateTime.format( formatter ) ).orElse( null );

        if ( content.startsWith( CMD ) )
        {
            String tex = content.substring( CMD.length() ).trim();

            System.out.printf( "[%s (%s) -> LaTeX] %s\n",
                    message.getAuthor().getName(), message.getAuthor().getStringID(), tex );
            try
            {
                TeXFormula formula = new TeXFormula( tex );

                BufferedImage image = (BufferedImage) formula
                        .createBufferedImage( TeXConstants.STYLE_DISPLAY, settings.getImageSize(),
                                settings.getTextColor(), settings.getBackgroundColor() );

                try ( ByteArrayOutputStream outputStream = new ByteArrayOutputStream() )
                {
                    ImageIO.write( image, "png", outputStream );

                    try ( InputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray() ) )
                    {
                        messageSent = new MessageBuilder( client )
                                .withChannel( message.getChannel() )
                                .withFile( inputStream, "tex.png" )
                                .withContent( String.format( "<@%d>: `%s` Created at: %s%s",
                                        message.getAuthor().getLongID(), tex, creationTime, editTime != null ?
                                                String.format( " (Last edited: %s)", editTime ) : "" ) ).build();
                    }
                }
            } catch ( ParseException e )
            {
                messageSent = new MessageBuilder( client ).withChannel( message.getChannel() )
                        .withContent( String.format( "<@%d>: **The input given at `%s` was unable to be rendered!**\n"
                                        + "**Reason(s):** ```Markdown\n%s```",
                                message.getAuthor().getLongID(), editTime != null ? editTime : creationTime,
                                formatErrorMessage( e.getMessage() ) ) ).build();
            }
            replies.put( message.getLongID(), messageSent );
        } else
        {
            Matcher matcher = pattern.matcher( content );

            while ( matcher.find() )
            {
                renderLaTex( new MessageBuilder( client ).withChannel( message.getChannel() ).withContent(
                        matcher.group( 1 ) ).build() );
            }
            return;
        }
    }

    private String formatErrorMessage(String errorMessage)
    {
        String[] reasons = errorMessage.split( "\n" );

        StringJoiner stringJoiner = new StringJoiner( "\n" );

        for ( int i = 0; i < reasons.length; i++ )
            stringJoiner.add( String.format( "%d. %s", i + 1, reasons[ i ] ) );

        return stringJoiner.toString();
    }
}
