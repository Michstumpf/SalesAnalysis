package com.ilegra.salesanalysis.SalesAnalysis.Application;
import com.ilegra.salesanalysis.SalesAnalysis.Model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.*;
import java.util.regex.Pattern;

public class FileManager {
    public static void main(String[] args) throws IOException, InterruptedException {

        WatchService watchService = FileSystems.getDefault().newWatchService();

        String homePath = "C:\\" + System.getenv("HOMEPATH");
        Path path = Paths.get(homePath + "\\data\\in");
        ReadAllFiles(homePath);

        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        System.out.println("Watch service registered for dir: " + path.toString());
        WatchKey key = null;

        while (true) {
            try {
                System.out.println("Waiting for key to be signalled...");
                key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Kind<?> kind = event.kind();
                    ReadAllFiles(homePath);
                    System.out.println("Event on " + event.context() + " is " + kind);
                }
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }

            boolean reset = key.reset();
            if (!reset)
                break;
        }
    }

    private static  void ReadAllFiles(String homePath) throws IOException, InterruptedException {
        File folder = new File(homePath + "\\data\\in");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                ReadFile(file, homePath);
                System.out.println(file.getName());
            }
        }
    }

    private static void ReadFile(File file, String homePath) throws IOException, InterruptedException {

        List<String> lines = null;

        Boolean readSucess = false;

        while ( !readSucess)
        {
            try {
                lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                readSucess = true;
                System.out.println("Success on read: " + file.getPath());
            }
            catch (Exception ex)
            {
                System.out.println("Error on read: " + file.getPath());
                Thread.sleep(1000);
            }
        }

        List<DataAnalysis> listDataAnalysis = new ArrayList<DataAnalysis>();

        for (String line: lines) {
            Pattern salesmanDataPattern = Pattern.compile("^001ç(\\d+)ç([\\w\\s]+)ç(\\d+.?\\d+)");
            Pattern customerDataPattern = Pattern.compile("^002ç(\\d+)ç([\\w\\s]+)ç(\\w+)");
            Pattern salesDataPattern = Pattern.compile("^003ç(\\d+)ç\\[.*(((\\d+-\\d+-(\\d+?\\d+))?)+)\\]ç\\w+");

            if(salesmanDataPattern.matcher(line).find())
            {
                Salesman salesman = new Salesman(line);
                listDataAnalysis.add(salesman);

                System.out.println("Is salesman:" +line );
            }else if(customerDataPattern.matcher(line).find())
            {
                Customer customer = new Customer(line);
                listDataAnalysis.add(customer);

                System.out.println("Is customer:" +line );
            }else if(salesDataPattern.matcher(line).find())
            {
                Sales sales = new Sales(line);
                listDataAnalysis.add(sales);

                System.out.println("Is salesData:" +line );
            }
        }

        Integer count = listDataAnalysis.size();

        WriteFile(listDataAnalysis,homePath, file.getName());


        file.renameTo(new File(homePath+"\\data\\out\\"+file.getName().replace(".dat",".readed.dat")));

        file.delete();
    }

    private static void WriteFile(List<DataAnalysis>  listDataAnalysis, String homePath, String fileName) throws IOException {
        File file = new File(homePath+"\\data\\out\\" + fileName.replace(".dat",".done.dat"));

        Integer amountClients = 0;
        Integer amountSalesman = 0;
        Map<String, Float> worstSalesman = new HashMap<String, Float>();
        Map<String, Float> mostExpansive = new HashMap<String, Float>();
        String mostExpansiveSalesID = null;

        for (DataAnalysis dataItem :
                listDataAnalysis) {

            if(dataItem.getDataAnalysisTypeEnum()== DataAnalysisTypeEnum.CustomerData)
                amountClients++;

            if(dataItem.getDataAnalysisTypeEnum()== DataAnalysisTypeEnum.SalesmanData)
                amountSalesman++;
            
            if(dataItem.getDataAnalysisTypeEnum()== DataAnalysisTypeEnum.SalesData) {

                Sales sales = ((Sales)dataItem);

                if(!worstSalesman.containsKey(sales.SalesmanName))
                    worstSalesman.put(sales.SalesmanName, Float.valueOf("0"));

                if(!worstSalesman.containsKey(sales.SalesID))
                    mostExpansive.put(sales.SalesID, Float.valueOf("0"));

                for (SalesItem si : sales.Items) {
                    worstSalesman.put(sales.SalesmanName, (worstSalesman.get(sales.SalesmanName) + (si.Quantity * si.Price)));
                    mostExpansive.put(sales.SalesID, (mostExpansive.get(sales.SalesID) + ( si.Quantity * si.Price)));
                }   
            }
        }

        String worstSalesmanName = null;
        Float worstSalesmanAmount = null;

        for ( Map.Entry<String, Float> entry : worstSalesman.entrySet() ) {
            String key = entry.getKey();
            Float value = entry.getValue();

            if(worstSalesmanName == null)
            {
                worstSalesmanName = key;
                worstSalesmanAmount = value;
            }

            if(value < worstSalesmanAmount)
            {
                worstSalesmanName = key;
                worstSalesmanAmount = value;
            }
        }

        mostExpansiveSalesID = null;
        Float mostExpansiveAmount = null;

        for ( Map.Entry<String, Float> entry : mostExpansive.entrySet() ) {
            String key = entry.getKey();
            Float value = entry.getValue();

            if(mostExpansiveSalesID == null)
            {
                mostExpansiveSalesID = key;
                mostExpansiveAmount = value;
            }

            if(value > mostExpansiveAmount)
            {
                mostExpansiveSalesID = key;
                mostExpansiveAmount = value;
            }
        }

        List<String> lines = Arrays.asList(
                "Amount of clients in the input file: " + amountClients,
                "Amount of salesman in the input file: " + amountSalesman,
                "ID of the most expensive sale: " + mostExpansiveSalesID,
                "Worst salesman ever: "+ worstSalesmanName);

        Files.write(file.toPath(),lines, StandardCharsets.UTF_8);
    }
}