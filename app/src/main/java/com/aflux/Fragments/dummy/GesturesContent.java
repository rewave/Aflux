package com.aflux.Fragments.dummy;

import com.aflux.Core.Gesture;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GesturesContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<GesturesItem> ITEMS = new ArrayList<GesturesItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, GesturesItem> ITEM_MAP = new HashMap<String, GesturesItem>();

    static {
        ParseQuery<ParseObject> =
        addItem(new GesturesItem("1", "Item 1"));
        addItem(new GesturesItem("2", "Item 2"));
        addItem(new GesturesItem("3", "Item 3"));
    }

    private static void addItem(GesturesItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.objectId, item);
    }

    public static class GesturesItem {
        public String objectId;
        public String name;

        public GesturesItem(String objectId, String name) {
            this.objectId = objectId;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
