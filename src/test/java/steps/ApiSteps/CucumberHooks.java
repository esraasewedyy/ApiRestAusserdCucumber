package steps.ApiSteps;


import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class CucumberHooks {

    @BeforeClass
    @Before
    public void setup() {
        RestAssured.baseURI = "https://simple-books-api.glitch.me";
    }
}