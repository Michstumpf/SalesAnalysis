package com.ilegra.salesanalysis.SalesAnalysis.Model;

public class Customer extends DataAnalysis {
    public Customer(String dataline) {
        super(DataAnalysisTypeEnum.CustomerData);

        String[] properties = dataline.split("ç");

        //TypeID
        super.setTypeID(properties[0]);

        //CPF
        this.CNPJ = properties[1];

        //Name
        this.Name = properties[2];

        //Salary
        this.BusinessArea = properties[3];
    }
    public String CNPJ;
    public  String Name;
    public  String BusinessArea;
}
