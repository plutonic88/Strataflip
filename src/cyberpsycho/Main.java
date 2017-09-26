package cyberpsycho;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Main {
	
	
	
	
	public static void main(String[] args) throws Exception 
	{
		Strataflip.loadassignedgames();
		Strataflip.loadsurveyanswers();
		
		Strataflip.loadgamehistory();
		
		
		Strataflip.computeDarkTriad();
		
		System.out.println("X");
	}

}
