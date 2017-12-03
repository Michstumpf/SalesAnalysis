package com.ilegra.salesanalysis.SalesAnalysis.Model;

public class SalesItem {

    public SalesItem(String dataitem){
        String[] properties = dataitem.split("-");
        ItemID = Integer.parseInt(properties[0]);
        Quantity =  Integer.parseInt(properties[1]);
        Price = Float.parseFloat(properties[2]);
    }

    public int ItemID;
    public int Quantity;
    public Float Price;
}
