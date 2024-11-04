package com.chess.application.client.components;

import java.util.prefs.Preferences;

public class PreferencesRemover
{
    public static void removePreferences()
    {
        try
        {
            Preferences preferences = Preferences.userNodeForPackage(SignInPage.class);
            preferences.clear();
        } catch (Exception e)
        {

        }
    }
}