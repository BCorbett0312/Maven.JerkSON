package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.List;


public class GroceryReporter {
    private final String originalFileText;
    ItemParser parser = new ItemParser();

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {
        List<Item> toTurnReadable = parser.parseItemList(originalFileText);
        System.out.println(parser.exceptionCounter);

        for (Item item: toTurnReadable) {
            System.out.println(item);
        }


        return null;
    }
}
