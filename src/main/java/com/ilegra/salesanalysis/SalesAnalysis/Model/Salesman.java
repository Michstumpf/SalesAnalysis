package com.ilegra.salesanalysis.SalesAnalysis.Model;

public class Salesman extends DataAnalysis{
    public Salesman(String dataline)
    {
        super(DataAnalysisTypeEnum.SalesmanData);

        String[] properties = dataline.split("ç");

        //TypeID
        super.setTypeID(properties[0]);

        //CPF
        this.CPF = properties[1];

        //Name
        this.Name = properties[2];

        //Salary
        this.Salary = Float.parseFloat(properties[3]);
    }

    public String CPF;
    public  String Name;
    public Float Salary;
}
