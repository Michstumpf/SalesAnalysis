package com.ilegra.salesanalysis.SalesAnalysis.Model;

public class DataAnalysis {
    public DataAnalysis(DataAnalysisTypeEnum type){
        DataAnalysisTypeEnum = type;
    }

    private String TypeID;
    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String value){
        TypeID = value;
    }

    private DataAnalysisTypeEnum DataAnalysisTypeEnum;
    public DataAnalysisTypeEnum getDataAnalysisTypeEnum(){
        return DataAnalysisTypeEnum;
    }
}
