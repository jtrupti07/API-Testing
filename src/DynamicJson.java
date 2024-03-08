import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import files.Payload;
import files.ReUsableMethods;

public class DynamicJson {

	@Test(dataProvider = "BooksData")
	public void addBook(String isbnNumber, String aisleNumber) {
		String bookName = "Selenium Automation " + RandomStringUtils.randomAlphabetic(3);
//		String isbnNumber = RandomStringUtils.randomAlphabetic(3).toLowerCase();
//		String aisleNumber = RandomStringUtils.randomNumeric(3);
		String authorName = "Josh Chen";
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
				.body(Payload.addBook(bookName, isbnNumber, aisleNumber, authorName)).when()
				.post("/Library/Addbook.php").then().log().all().assertThat().statusCode(200).extract().response()
				.asString();

		JsonPath js = ReUsableMethods.rawToJson(response);
		String responseMessage = js.get("Msg");
		System.out.println(responseMessage);
		String id = js.get("ID");
		System.out.println(id);

		// delete book
		given().header("Content-Type", "application/json").body("{\r\n" + "    \"ID\" : \"" + id + "\"\r\n" + "}")
				.when().post("/Library/DeleteBook.php").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("book is successfully deleted"));

	}

	// TestNG data provider for parameterization
	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		// Provide multiple data sets to add books api
		return new Object[][] {
				{ RandomStringUtils.randomAlphabetic(3).toLowerCase(), RandomStringUtils.randomNumeric(3) },
				{ RandomStringUtils.randomAlphabetic(3).toLowerCase(), RandomStringUtils.randomNumeric(3) },
				{ RandomStringUtils.randomAlphabetic(3).toLowerCase(), RandomStringUtils.randomNumeric(3) } };
	}
}
