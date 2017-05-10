package com.klimchuk.mapsloader.data;

import android.content.Context;

import com.klimchuk.mapsloader.utils.Parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by alexey on 05.05.17.
 */

public class StaticDataCache {

    private static Region currentRegion;

    public static Region getRegion() {
        return currentRegion;
    }

    public static void loadRegions(Context context) {
        try {
            currentRegion = Parser.parseRegions(context);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentRegion(Region currentRegion) {
        StaticDataCache.currentRegion = currentRegion;
    }
}
