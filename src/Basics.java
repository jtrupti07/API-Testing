import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import files.Payload;
import files.ReUsableMethods;

public class Basics {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// Rest assured all works on Given, When and Then

		// Given: All input details
		// When: Submit the API (resource and http request type should came under when
		// Then: Validate the response

		// Add place (Post) API
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String postMethodResponse = given().log().all().queryParam("key", "qaclick123")
				.header("Content-Type", "application/json").body(Payload.addPlace()).when()
				// In this way we handle static json payloads
				// .header("Content-Type", "application/json").body(Files.readAllBytes(Paths.get("C:\\Users\\Trupti.Jadhav1\\Desktop\\addplaces.json"))).when()
				.post("/maps/api/place/add/json").then().assertThat().statusCode(200).body("scope", equalTo("APP"))
				.header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		System.out.println(postMethodResponse);

		JsonPath js = new JsonPath(postMethodResponse); // for parsing json
		String placeId = js.getString("place_id");
		System.out.println(placeId);

		// update place(Put) API

		String putMethodResponse = given().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"70 winter walk, USA\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}")
				.when().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated")).extract().response().asString();

		System.out.println(putMethodResponse);
		JsonPath updateJs = new JsonPath(putMethodResponse); // for parsing json
		String responseMessage = updateJs.getString("msg");
		System.out.println(responseMessage);
		assertEquals(responseMessage, "Address successfully updated");

		// Get place API
		String getMethodResponse = given().queryParam("key", "qaclick123").queryParam("place_id", placeId).when()
				.get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200)
				.body("address", equalTo("70 winter walk, USA")).extract().response().asString();
		System.out.println(getMethodResponse);
		JsonPath getJs = ReUsableMethods.rawToJson(getMethodResponse);
		assertEquals(getJs.getString("location.latitude"), "-38.383494");
		assertEquals(getJs.getString("accuracy"), "50");
	}

}
