package com.effective_light.latex_bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings
{
    private Properties settings;

    public Settings() throws IOException
    {
        this.settings = new Properties();
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

    }

    public String getToken()
    {
        return settings.getProperty( "token" );
    }


}
