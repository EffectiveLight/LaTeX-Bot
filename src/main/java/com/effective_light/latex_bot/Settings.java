package com.effective_light.latex_bot;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings
{

    private String token;
    private float imageSize;
    private Color textColor;
    private Color backgroundColor;

    public Settings() throws IOException
    {
        Properties settings = new Properties();
        String fileName = "settings.properties";
        File propFile = new File( fileName );

        if ( !propFile.exists() )
        {
            settings.setProperty( "token", "token_plz" );
            settings.setProperty( "image-size", "20.0" );
            settings.setProperty( "text-color", "219,219,219" );
            settings.setProperty( "background-color", "54,57,62" );
            settings.store( new FileOutputStream( propFile ), "LaTeX-Bot Setting(s)" );
            System.out.printf( "Please, modify the %s file as desired.\n", fileName );
            System.exit( 1 );
        }

        settings.load( new FileInputStream( propFile ) );

        this.token = settings.getProperty( "token" );
        this.imageSize = Float.parseFloat( settings.getProperty( "image-size" ) );

        String[] textComponents = settings.getProperty( "text-color" ).split( "," );
        int[] textRgb = new int[ 3 ];

        for ( int i = 0; i < textComponents.length; i++ )
        {
            textRgb[ i ] = Integer.parseInt( textComponents[ i ] );
        }

        this.textColor = new Color( textRgb[ 0 ], textRgb[ 1 ], textRgb[ 2 ] );

        String[] backgroundComponents = settings.getProperty( "background-color" ).split( "," );
        int[] backgroundRgb = new int[ 3 ];

        for ( int i = 0; i < backgroundComponents.length; i++ )
        {
            backgroundRgb[ i ] = Integer.parseInt( backgroundComponents[ i ] );
        }

        this.backgroundColor = new Color( backgroundRgb[ 0 ], backgroundRgb[ 1 ], backgroundRgb[ 2 ] );
    }

    public String getToken()
    {
        return token;
    }

    public float getImageSize()
    {
        return imageSize;
    }

    public Color getTextColor()
    {
        return textColor;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }
}
