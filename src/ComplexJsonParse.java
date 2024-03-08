import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//To access complex Json
		
		//Print number of courses returned by API
		JsonPath js = new JsonPath(Payload.complexJson());
		int coursesSize = js.getInt("courses.size()");
		System.out.println(coursesSize);
		
		//Print purchase amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//Print title of first course
		String titleOfFirstCourse = js.getString("courses[0].title");
		System.out.println(titleOfFirstCourse);
		
		// Print All course titles and their respective Prices
		for(int i=0; i<coursesSize; i++)
		{
			String titleOfCourse = js.getString("courses["+ i +"].title");
			String priceOfCourse = js.getString("courses["+ i +"].price");
			System.out.println("title is "+titleOfCourse+" and it's price is "+priceOfCourse);
			break;
		}
		
		//Print number of copies sold by RPA course
		for(int i=0; i<coursesSize; i++)
		{
			String titleOfCourse = js.getString("courses["+ i +"].title");
			if(titleOfCourse.equals("RPA"))
			{
				int copiesSoldForRPA = js.get("courses["+ i +"].copies");
				System.out.println(copiesSoldForRPA);
				break;
			}
					
		}
				
		//Verify sum of all the courses matches with purchase amount
		int actualPurchaseAmount = 0;
		for(int i=0; i<coursesSize; i++)
		{
			int priceOfCourse = js.getInt("courses["+ i +"].price");
			int copiesOfCourse = js.get("courses["+ i +"].copies");
			int amount = priceOfCourse * copiesOfCourse;
			actualPurchaseAmount = actualPurchaseAmount + amount;			
		}
		System.out.println(actualPurchaseAmount);
		Assert.assertEquals(actualPurchaseAmount, purchaseAmount);
		
	}

}
