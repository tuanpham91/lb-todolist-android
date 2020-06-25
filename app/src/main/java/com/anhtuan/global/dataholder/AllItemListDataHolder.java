package com.anhtuan.global.dataholder;

import java.util.ArrayList;

public class AllItemListDataHolder {
    private static ArrayList<String> allUniqueItemList;

    public static ArrayList<String> getAllUniqueItemList() {
        if (getAllUniqueItemList() == null) {
            allUniqueItemList = new ArrayList<>();
        }
        return allUniqueItemList;
    }

    public static void setAllUniqueItemList(ArrayList<String> newUniqueItemList) {
        allUniqueItemList = newUniqueItemList;
    }
}
