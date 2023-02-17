package dev.despg.examples.gravelshipping;

import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;

/*public class Reader {

	// NOT FINAL
	private static final String COMMA_DELIMITER = ",";

	public static ArrayList<String> loadCoordinates(String filepath) throws FileNotFoundException{

		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(filepath));) {
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
		}
		return records;
	}

	private static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(COMMA_DELIMITER);
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}
}*/

public class Reader {
    public static void main(String[] args) {
        ArrayList<String> values = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/despgutils/Routes.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                values.add(line);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println(values);
        System.out.println(values.get(2).);
    }
}
