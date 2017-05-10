package com.klimchuk.mapsloader.utils;

import android.content.Context;

import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.data.Region;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by alexey on 04.05.17.
 */

public class Parser {

    public static Region parseRegions(Context context) throws XmlPullParserException, IOException {
        Region currentRegion = new Region("root");

        XmlPullParser parser = context.getResources().getXml(R.xml.regions);

        // Top level depth
        int depth = 2;

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && parser.getName().equals("region")) {

                int depthDifference = depth - parser.getDepth();

                switch (depthDifference) {

                    // If deepen in hierarchy
                    case -1:
                        currentRegion = currentRegion
                                .getNestedRegions()
                                .get(currentRegion.getNestedRegions().size() - 1);
                        currentRegion
                                .getNestedRegions()
                                .add(getRegionFromParser(parser, currentRegion));

                        break;

                    // If stay on the same level
                    case 0:
                        currentRegion
                                .getNestedRegions()
                                .add(getRegionFromParser(parser, currentRegion));
                        break;

                    // If upping in hierarchy
                    case 1:
                        currentRegion = currentRegion.getTopRegion();

                        currentRegion
                                .getNestedRegions()
                                .add(getRegionFromParser(parser, currentRegion));
                        break;
                    case 2:
                        currentRegion = currentRegion.getTopRegion();
                        currentRegion = currentRegion.getTopRegion();

                        currentRegion
                                .getNestedRegions()
                                .add(getRegionFromParser(parser, currentRegion));
                        break;
                }

                depth = parser.getDepth();
            }

            parser.next();
        }

        return currentRegion;
    }

    private static Region getRegionFromParser(XmlPullParser parser, Region topRegion) {
        Region region = new Region(parser.getAttributeValue(null, "name"), buildUrl(parser));

        // Region hasn't parent if it is on the top of hierarchy
        if (parser.getDepth() != 1 && topRegion != null) {
            region.setTopRegion(topRegion);
        }

        return region;
    }

    private static String buildUrl(XmlPullParser parser) {
        // TODO: provide logic of url`s building.
        return "Iceland_europe_2.obf.zip";
    }

}
