package com.ilegra.salesanalysis.SalesAnalysis.Model;

import java.util.ArrayList;
import java.util.List;

public class Sales extends DataAnalysis {
    public Sales(String dataline) {
        super(DataAnalysisTypeEnum.SalesData);

        String[] properties = dataline.split("ç");

        //TypeID
        super.setTypeID(properties[0]);

        //SalesID
        this.SalesID = properties[1];

        //Items
        String listItemString = properties[2];
        listItemString = listItemString.replace("[","");
        listItemString = listItemString.replace("]","");

        Items = new ArrayList<SalesItem>();

        for (String itemString:
             listItemString.split(",")) {

            SalesItem salesItem = new SalesItem(itemString);
            Items.add(salesItem);
        }

        //Salary
        this.SalesmanName = properties[3];
    }

    public String SalesID;
    public List<SalesItem> Items;
    public String SalesmanName;
}
