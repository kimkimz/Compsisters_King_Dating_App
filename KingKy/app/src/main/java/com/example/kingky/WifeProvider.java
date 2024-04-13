package com.example.kingky;

import java.util.ArrayList;

public class WifeProvider {
    public static ArrayList<Wives> generateWives(){
        ArrayList<Wives> wivesList = new ArrayList<Wives>();

        //wivesList.add(new Wives("Catherine of Aragon","toTwentyFive", 25, "", R.drawable.wife1));
        wivesList.add(new Wives("Anne Boleyn", "toThirty", 26, "", R.drawable.wife2));
        wivesList.add(new Wives("Mary Tudor", "toTwenty",19, "", R.drawable.wife3));
        wivesList.add(new Wives("Elizabeth of York", "toTwentyFive",22, "", R.drawable.wife4));
        wivesList.add(new Wives("Jane Seymour", "toThirty",28, "", R.drawable.wife5));
        wivesList.add(new Wives("Margaret Beaufort", "toTwenty",20, "", R.drawable.wife6));
        wivesList.add(new Wives("Isabella of Castile", "toTwentyFive",24, "", R.drawable.wife7));
        wivesList.add(new Wives("Eleanor of Aquitaine", "toThirty",26, "", R.drawable.wife8));
        wivesList.add(new Wives("Anne of Cleves", "thirtyPlus",31, "", R.drawable.wife9));
        wivesList.add(new Wives("Catherine Howard", "toTwenty",17, "", R.drawable.wife10));

        return wivesList;
    }

}
