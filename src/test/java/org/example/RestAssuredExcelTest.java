package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.ExcelUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredExcelTest {

    public static void main(String[] args) throws IOException {
        RestAssured.baseURI = "http://example.com/api";

        ExcelUtils excelUtils = new ExcelUtils("path/to/excel.xlsx", "Sheet1");
        List<Map<String, String>> testData = excelUtils.getAllData();

        for (Map<String, String> row : testData) {
            String id = row.get("ID");
            String name = row.get("Name");
            String userName = row.get("UserName");

            Response response = given()
                    .param("id", id)
                    .param("name", name)
                    .param("username", userName)
                    .when()
                    .get("/users")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            // Perform other actions and assertions as needed
        }

        excelUtils.closeWorkbook();
    }
}