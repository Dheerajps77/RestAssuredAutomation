package org.example;

import utils.ExcelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadExcelValue {


    public static void readValueWithoutLinkedList() throws IOException {
        try {

            String fileName = "CreateAirlineData";
            String filePath = "src/test/resources/testdata/" + fileName + ".xlsx";
            ExcelUtils excelUtils = new ExcelUtils(filePath, "Sheet1");
            List<Map<String, String>> mapOfList = excelUtils.getAllData();

            Iterator<Map<String, String>> listOfMap = mapOfList.iterator();

            while (listOfMap.hasNext()) {
                Map<String, String> value = listOfMap.next();
                System.out.println(value);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void readValueWithLinkedList() throws IOException {
        try {
            String fileName = "CreateAirlineData";
            String filePath = "src/test/resources/testdata/" + fileName + ".xlsx";
            List<LinkedHashMap<String, String>> linkedHashMaps = ExcelUtils.getExcelDataAsListOfMapAllDatas(fileName, "Sheet1");
            Iterator<LinkedHashMap<String, String>> stringLinkedHashMap = linkedHashMaps.iterator();
            while (stringLinkedHashMap.hasNext()) {
                LinkedHashMap<String, String> linkedHashMap = stringLinkedHashMap.next();
                if (linkedHashMap.get("Enabled") != null && linkedHashMap.get("Enabled").equalsIgnoreCase("Y")) {
                    System.out.println(linkedHashMap);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void readValueBasisOnRows() throws IOException {
        String fileName = "CreateAirlineData";
        String filePath = "src/test/resources/testdata/" + fileName + ".xlsx";
        try {
            List<Map<String, String>> valueOfMap = ExcelUtils.getExcelDataAsListOfMapBasisOnTestSetsRowsWise(fileName, "Sheet2", "Test Set 4");
            Iterator<Map<String, String>> mapIterator = valueOfMap.iterator();

            while (mapIterator.hasNext()) {
                Map<String, String> mapValuess = mapIterator.next();
                System.out.println(mapValuess);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        //readValueWithLinkedList();
        readValueBasisOnRows();
    }
}
