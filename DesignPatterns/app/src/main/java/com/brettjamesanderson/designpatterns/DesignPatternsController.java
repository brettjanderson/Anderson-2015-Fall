package com.brettjamesanderson.designpatterns;

import android.content.Context;
import android.content.res.Resources;
import java.util.*;

public class DesignPatternsController {
    private static DesignPatternsController ourInstance = new DesignPatternsController();
    private static LinkedList<DesignPattern> designPatternList;
    private static LinkedList<DesignPattern> designPatternReturnedList;

    public void initController(Context currentContext) {
    }

    private DesignPatternsController() {
    }

    public static DesignPattern getNewDesignPattern(Context context){

        if(designPatternList == null){
            designPatternList = new LinkedList<DesignPattern>();
            String[] designPatternNames = context.getResources().getStringArray(R.array.designPatternNames);
            String[] designPatternDescriptions = context.getResources().getStringArray(R.array.designPatternDescriptions);
            String[] designPatternFavorites = context.getResources().getStringArray(R.array.designPatternFavorites);
            for(int i = 0; i < designPatternNames.length; i++)
                designPatternList.add(new DesignPattern(designPatternNames[i], designPatternDescriptions[i]));

        }

        if(designPatternReturnedList == null){
            designPatternReturnedList = new LinkedList<DesignPattern>();
        }

        if(designPatternList.isEmpty()) {
            designPatternList.addAll(designPatternReturnedList);
            Collections.shuffle(designPatternList);
        }

        DesignPattern toReturn = designPatternList.removeFirst();
        return toReturn;
    }

    public static void putDesignPatternBack(DesignPattern pattern){
        designPatternReturnedList.addFirst(pattern);
    }
}
