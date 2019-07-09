package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;


import java.util.*;


public class GroceryReporter {
    private final String originalFileText;
    private ItemParser parser = new ItemParser();



    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    public Map<String, Map> makeLinkedHashMap(){
        List<Item> itemList = parser.parseItemList(originalFileText);
        Map<String, Map> resultMap = new LinkedHashMap<>();
        for(Item item : itemList){
            boolean inserted = false;
            String name = item.getName();
            Set<String> nameSet = resultMap.keySet();
            for(String key : nameSet){
                if(name.equals(key)){
                    putPriceAndCount(item, resultMap.get(key));
                    inserted = true;
                }
            }
            if(!inserted){
                Map<Double, Integer> priceMap = new LinkedHashMap<>();
                priceMap.put(item.getPrice(), 1);
                resultMap.put(item.getName(), priceMap);
            }
        }

        return resultMap;
    }


    public void putPriceAndCount(Item item, Map<Double, Integer> priceAndCount){
        boolean inserted = false;
        Set<Double> priceSet = priceAndCount.keySet();
        for(Double price: priceSet){
            if(price.equals(item.getPrice())){
                Integer counter = priceAndCount.get(price);
                priceAndCount.put(price, ++counter);
                inserted = true;
            }
        }
        if(!inserted){
            priceAndCount.put(item.getPrice(), 1);
        }
    }

    public String timeOrTimes(Integer count){
        if(count > 1){
            return "s";
        }
        else return "";
    }



    @Override
    public String toString() {
        Map<String, Map> completeMap = makeLinkedHashMap();
        Set<String> itemNameSet = completeMap.keySet();
        String result = "";
        for(String name : itemNameSet){
            Integer total = 0;
            Map<Double, Integer> priceCounts = completeMap.get(name);
            Set<Double> priceSet = priceCounts.keySet();
            String priceString = "";
            Integer counterForLines = 0;
            for(Double price: priceSet){

                priceString += String.format("Price: \t %3.2f\t\t seen: %d time%s",
                        price, priceCounts.get(price), timeOrTimes(priceCounts.get(price)))+"\n";
                if(counterForLines == 0){
                    priceString += "-------------\t\t -------------\n";
                    counterForLines++;
                }

                total += priceCounts.get(price);
            }
            result += String.format("name:%8s \t\t seen: %d time%s", name, total, timeOrTimes(total)) +
                    "\n"+ "============= \t \t =============\n";
            result += priceString + "\n";

        }
        result += "Errors         \t \t seen: " + parser.exceptionCounter + " times\n";





//        UGH THIS STRING SUCKS
//        String actual = "name:    Milk \t\t seen: 6 times\n" +
//                "============= \t \t =============\n" +
//                "Price: \t 3.23\t\t seen: 5 times\n" +
//                "-------------\t\t -------------\n" +
//                "Price: \t 1.23\t\t seen: 1 time\n" +
//                "\n" +
//                "name:   Bread \t\t seen: 6 times\n" +
//                "============= \t \t =============\n" +
//                "Price: \t 1.23\t\t seen: 6 times\n" +
//                "-------------\t\t -------------\n" +
//                "\n" +
//                "name: Cookies \t\t seen: 8 times\n" +
//                "============= \t \t =============\n" +
//                "Price: \t 2.25\t\t seen: 8 times\n" +
//                "-------------\t\t -------------\n" +
//                "\n" +
//                "name:  Apples \t\t seen: 4 times\n" +
//                "============= \t \t =============\n" +
//                "Price: \t 0.25\t\t seen: 2 times\n" +
//                "-------------\t\t -------------\n" +
//                "Price: \t 0.23\t\t seen: 2 times\n" +
//                "\n" +
//                "Errors         \t \t seen: 4 times\n";




        return result;
    }
}
