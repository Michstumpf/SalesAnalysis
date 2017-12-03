package com.ilegra.salesanalysis.SalesAnalysis;

import com.ilegra.salesanalysis.SalesAnalysis.Application.FileManager;
import java.io.IOException;

public class SalesAnalysisApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		FileManager fileManager = new FileManager();
		fileManager.main(args);
	}
}
