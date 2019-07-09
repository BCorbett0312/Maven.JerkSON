package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    Pattern pattern;
    Matcher matcher;
    Integer exceptionCounter = 0;


    public List<Item> parseItemList(String valueToParse) {
        pattern = pattern.compile("##");
        List<Item> listOfItems = new ArrayList<>();
        String[] stringArrayOfItems = pattern.split(valueToParse);
        for(String str: stringArrayOfItems){
            try {
                listOfItems.add(parseSingleItem(str));
            } catch (ItemParseException e){

            }

        }

        return listOfItems;
    }



    public Item parseSingleItem(String singleItem)throws ItemParseException {
        String fixed =fixInput(singleItem);

        String name;
        Double price;
        String type;
        String expiration;

        try {
            name = parseName(fixed).toLowerCase();
            if(name.length() == 0){
                exceptionCounter++;
                throw new ItemParseException();
            }
            if(name.equals("co0kies")){
                name = "cookies";
            }
        } catch (NullPointerException e){
            exceptionCounter++;
            throw new ItemParseException();

        }

        try{
            price = parsePrice(fixed);
        } catch (NullPointerException | NumberFormatException e){
            exceptionCounter++;
            throw new ItemParseException();
        }


        try{
            type = parseType(fixed).toLowerCase();
        } catch (NullPointerException e){
            exceptionCounter++;
            throw new ItemParseException();
        }

        try{
            expiration = parseExpiration(fixed).toLowerCase();
        } catch (NullPointerException e){
            exceptionCounter++;
            throw new ItemParseException();
        }






        return new Item(capitalizeItemName(name), price, type, expiration);
    }

    private String parseName(String toBeParsed)  {
        pattern = Pattern.compile("(?i:.*name:(.*?);)");
        matcher = pattern.matcher(toBeParsed);


        if(matcher.find()){
            return matcher.group(1);
        }
        return null;


    }

    private Double parsePrice(String toBeParsed) {
        pattern = pattern.compile("(?i:.*price.(.*?);)");
        matcher = pattern.matcher(toBeParsed);
        if(matcher.find()){
            return Double.parseDouble(matcher.group(1));
        }

        return null;
    }

    private String parseType(String toBeParsed) {
        pattern = pattern.compile("(?i:.*type:(.*?);)");
        matcher = pattern.matcher(toBeParsed);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    private String parseExpiration(String toBeParse)  {
        pattern = pattern.compile("(?i:.*expiration:(\\d{1,2}/\\d{2}/\\d{4}))");
        matcher = pattern.matcher(toBeParse);
        if(matcher.find()){
            return matcher.group(1);
        }

        return null;
    }



    private String fixInput(String toFix){
        pattern = pattern.compile("@|\\^|\\*|%|!");
        matcher = pattern.matcher(toFix);
        String result = matcher.replaceAll(":");

        pattern = pattern.compile("Food(.*)expiration");
        matcher = pattern.matcher(result);
        String ultimateFix = matcher.replaceAll("Food;expiration");

        return ultimateFix;
    }

    private String capitalizeItemName(String toCapitalize){
        return "" + ((Character) toCapitalize.charAt(0)).toString().toUpperCase() + toCapitalize.substring(1);
    }


}
