package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        plugin = {"pretty", "html:target/report/cucumber.html",
                "json:target/report/cucumber.json"}
)
public class CucumberTestNGRunner extends AbstractTestNGCucumberTests {
}
