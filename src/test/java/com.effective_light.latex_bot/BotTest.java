package com.effective_light.latex_bot;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotTest
{

    @Test
    public void testPattern()
    {
        Pattern pattern = Pattern.compile( "`(!tex\\s.*?)`" );
        String test = "Hi, `!tex \\text{hi}` idk `!tex x = 1+6 \\neq 8`.";
        Matcher matcher = pattern.matcher( test );

        List<String> matches = new ArrayList<>();

        while ( matcher.find() )
        {
            matches.add( matcher.group( 1 ) );
        }

        Assert.assertTrue( matches.size() == 2 );
        Assert.assertTrue( matches.get( 0 ).equals( "!tex \\text{hi}" ) );
        Assert.assertTrue( matches.get( 1 ).equals( "!tex x = 1+6 \\neq 8" ) );
    }
}
