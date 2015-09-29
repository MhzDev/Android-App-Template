package com.mhzdev.apptemplate.Services.IappPurchase;


import java.util.ArrayList;
import java.util.List;

public class SkuList {
    public static final String SKU_1 = "100";
    public static final String SKU_2 = "200";
    public static final String SKU_3 = "300";
    public static final String SKU_4 = "400";
    public static final String SKU_5 = "500";
    public static final String SKU_6 = "600";
    public static final String SKU_7 = "700";
    public static final String SKU_8 = "800";
    public static final String SKU_9 = "900";
    public static final String SKU_10 = "1000";

    public static List<String> getSkuList(){
        List<String> skuList = new ArrayList<>();
        skuList.add(SKU_1);
        skuList.add(SKU_2);
        skuList.add(SKU_3);
        skuList.add(SKU_4);
        skuList.add(SKU_5);
        skuList.add(SKU_6);
        skuList.add(SKU_7);
        skuList.add(SKU_8);
        skuList.add(SKU_9);
        skuList.add(SKU_10);

        return skuList;
    }
}
