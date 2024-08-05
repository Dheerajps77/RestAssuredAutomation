package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtils {

    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath, String sheetName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheet(sheetName);
    }

    public static Object[][] getExcelData(String filePath, String sheetName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                data[i - 1][j] = cell.toString();
            }
        }
        workbook.close();
        fileInputStream.close();
        return data;
    }

    /**
     * Get the data from a specific cell
     *
     * @param rowNum Row number (0-based)
     * @param colNum Column number (0-based)
     * @return Cell data as a string
     */
    public String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Get all data from the sheet as a list of maps
     *
     * @return List of maps, where each map represents a row with column names as keys
     */
    public List<Map<String, String>> getAllData() {
        List<Map<String, String>> allData = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        int numOfColumns = headerRow.getPhysicalNumberOfCells();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row currentRow = sheet.getRow(i);
            Map<String, String> currentRowData = new HashMap<>();

            for (int j = 0; j < numOfColumns; j++) {
                String columnName = headerRow.getCell(j).getStringCellValue();
                String cellValue = getCellData(i, j);
                currentRowData.put(columnName, cellValue);
            }

            allData.add(currentRowData);
        }

        return allData;
    }

    /**
     * Close the workbook
     */
    public void closeWorkbook() throws IOException {
        workbook.close();
    }

    public static List<LinkedHashMap<String, String>> getExcelDataAsListOfMapWithHavingRandomNumber(String excelFileName, String sheetName) throws IOException {
        List<LinkedHashMap<String, String>> dataFromExcel = new ArrayList<>();
        List<String> allKeys = new ArrayList<>();
        LinkedHashMap<String, String> mapData;
        DataFormatter dataFormatter = new DataFormatter();
        int size = 6;

        Workbook workbook = WorkbookFactory.create(new File("src/test/resources/testdata/" + excelFileName + ".xlsx"));
        Sheet sheet = workbook.getSheet(sheetName);

        int totalRows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < totalRows; i++) {
            mapData = new LinkedHashMap<>();
            if (i == 0) {
                int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
                for (int j = 0; j < totalCols; j++) {
                    allKeys.add(sheet.getRow(0).getCell(j).getStringCellValue());
                }
            } else {
                int totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                for (int j = 0; j < totalCols; j++) {
                    String cellValue = dataFormatter.formatCellValue(sheet.getRow(i).getCell(j));
                    if (cellValue.contains("RandomNumber")) {
                        // With size
                        if (cellValue.contains("_")) {
                            size = Integer.valueOf((cellValue.split("_"))[1]);
                        }
                        cellValue = RandomDataGenerator.getRandomNumber(size);
                    }
                    mapData.put(allKeys.get(j), cellValue);
                }
                dataFromExcel.add(mapData);
            }
        }
        return dataFromExcel;
    }


    public static List<LinkedHashMap<String, String>> getExcelDataAsListOfMapAllDatas(String excelFileName, String sheetName) throws IOException {
        List<LinkedHashMap<String, String>> dataFromExcel = new ArrayList<>();
        List<String> allKeys = new ArrayList<>();
        LinkedHashMap<String, String> mapData;
        DataFormatter dataFormatter = new DataFormatter();
        String cellValue = "";
        int totalCols, totalrows;
        Workbook workbook = WorkbookFactory.create(new File("src/test/resources/testdata/" + excelFileName + ".xlsx"));
        Sheet sheet = workbook.getSheet(sheetName);

        int totalRows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < totalRows; i++) {
            mapData = new LinkedHashMap<>();
            if (i == 0) {
                totalrows = sheet.getRow(0).getPhysicalNumberOfCells();
                for (int j = 0; j < totalrows; j++) {
                    allKeys.add(sheet.getRow(0).getCell(j).getStringCellValue());
                }
            } else {
                totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                for (int j = 0; j < totalCols; j++) {
                    Cell cellValue1 = sheet.getRow(i).getCell(j);
                    cellValue = dataFormatter.formatCellValue(cellValue1);
                    mapData.put(allKeys.get(j), cellValue);
                }
                dataFromExcel.add(mapData);
            }
        }
        return dataFromExcel;
    }


    public static List<Map<String, String>> getExcelDataAsListOfMapBasisOnTestSetsRowsWise(String excelFileName, String sheetName,
                                                                                           String rowName) throws IOException {
        List<LinkedHashMap<String, String>> dataFromExcel = new ArrayList<>();
        List<String> allKeys = new ArrayList<>();
        LinkedHashMap<String, String> mapData;
        DataFormatter dataFormatter = new DataFormatter();
        List<Map<String, String>> testData = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File("src/test/resources/testdata/" + excelFileName + ".xlsx"));
        Sheet sheet = workbook.getSheet(sheetName);

// Assuming the test set column index and other necessary variables are defined
        int testSetColumnIndex = 0; // Assuming the test set column is the first column

        int totalRows = sheet.getPhysicalNumberOfRows();
        for (int rowIndex = 0; rowIndex < totalRows; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue; // Skip null rows
            }

            Cell testSetCell = row.getCell(testSetColumnIndex);
            String testSetValue = "";
            if (testSetCell != null) {
                testSetValue = testSetCell.getStringCellValue().trim();
            }


            /*
            Conditional (Ternary) Operator (? :)
            If cell is null (cell == null evaluates to true), then cellValue is assigned an empty string "".
            If cell is not null (cell == null evaluates to false), then the expression after the : is evaluated.
             */
            // Check if the row belongs to the desired test sets
            if (testSetValue.equals(rowName)) {
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int colIndex = 1; colIndex < row.getLastCellNum(); colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    String cellValue = (cell == null) ? "" : dataFormatter.formatCellValue(cell).trim();
                    String columnHeader = sheet.getRow(0).getCell(colIndex).getStringCellValue().trim();
                    rowData.put(columnHeader, cellValue);
                }
                testData.add(rowData);
            }
        }
        return testData;
    }
}