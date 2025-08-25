package api.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVUtility {

    // Returns Object[][] for TestNG DataProvider
    public static Object[][] getCSVData(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allRows = reader.readAll();

            if (allRows.size() <= 1) return new Object[0][0]; // no data

            int rows = allRows.size() - 1; // skip header
            int cols = allRows.get(0).length;

            Object[][] data = new Object[rows][cols];
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                for (int j = 0; j < cols; j++) {
                    data[i - 1][j] = row[j].trim();
                }
            }
            return data;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }
}
