package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import steps.POJO.BodyModeling.ApiClient.ApiClientRequest;
import steps.POJO.BodyModeling.ApiClient.ApiClientResponse;
import steps.POJO.BodyModeling.Book.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClientStepDefinitions {


    private ApiClientRequest request;
    private Response response;

    @Given("I have a new API client request ClientName {string} and ClientEmail {string}")
    public void iHaveANewAPIClientRequestClientNameAndClientEmail(String ClientName, String ClientEmail) {

        request = new ApiClientRequest();
        request.setClientName(ClientName+"_" + System.currentTimeMillis());
        request.setClientEmail(System.currentTimeMillis()+ "_" + ClientEmail);
    }

    @When("I send a POST request to create an API client")
    public void i_send_a_post_request_to_create_an_api_client() {
        response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api-clients/");
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode.intValue());
    }

    @Then("the response should contain an access token")
    public void the_response_should_contain_an_access_token() {
        ApiClientResponse apiClientResponse = response.as(ApiClientResponse.class);
        Assert.assertNotNull(apiClientResponse.getAccessToken());
        Assert.assertFalse(apiClientResponse.getAccessToken().isEmpty());
    }








    @When("I send a GET request to fetch {string} books")
    public void iSendAGETRequestToFetchBooks(String type) {
        response = given()
                .queryParam("type", type)
                .when()
                .get("/books");

    }

    @Then("the response should contain a list of {string} books")
    public void the_response_should_contain_a_list_of_non_fiction_books(String type) {
        List<Book> books = response.jsonPath().getList("", Book.class);
        Assert.assertFalse(books.isEmpty(), "The list of books should not be empty");
        books.forEach(book -> {
            Assert.assertEquals(book.getType(), type, "All books should be " + type);
        });
    }






    @When("^I send a GET request to fetch book with ID (\\d+)$")
    public void iSendAGetRequestToFetchBookWitId(int bookId) {
        response = given()
                .when()
                .get("/books/" + bookId);
    }



    @Then("the response should contain details of the requested book")
    public void the_response_should_contain_details_of_the_requested_book() {
        Book book = response.as(Book.class);


    }

    @Then("I store the access token from the response")
    public void i_store_the_access_token_from_the_response() {
        storedAccessToken = response.jsonPath().getString("accessToken");
        Assert.assertNotNull(storedAccessToken, "Access token should not be null");
      System.out.println("Stored access token: " + storedAccessToken);  // For debugging
    }



    private int storedBookId;
    private String storedAccessToken;
    private String storedOrderId;

    public String getStoredAccessToken() {
        return storedAccessToken;
    }

    public int getStoredBookId() {
        return storedBookId;
    }

    public String getStoredOrderId() {
        return storedOrderId;
    }

    @Then("I store the book ID")
        public void i_store_the_book_id() {
            List<Map<String, Object>> books = response.jsonPath().getList("");
            Assert.assertFalse(books.isEmpty(), "The list of books should not be empty");

            storedBookId = (Integer) books.get(0).get("id");
            Assert.assertTrue(storedBookId > 0, "Stored book ID should be greater than 0");
            System.out.println("Stored book ID: " + storedBookId);  // For debugging
        }



    @When("I sent the book ID")
    public void i_sent_the_book_id() {
        System.out.println(getStoredBookId());
    }

    @When("I send a GET request to fetch book with ID")
    public void iSendAGetRequestToFetchBookWitId() {
        response = given()
                .when()
                .get("/books/" + getStoredBookId());
    }

    @When("I submit an order for the stored book ID with customer name {string}")
    public void i_submit_an_order_for_the_stored_book_id_with_customer_name(String customerName) {
        Assert.assertNotNull(storedAccessToken, "Access token should not be null");

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("bookId", storedBookId);
        orderRequest.put("customerName", customerName);

        response = given()
                .auth().oauth2(storedAccessToken)
                .contentType("application/json")
                .body(orderRequest)
                .when()
                .post("/orders");

    }


    @When("I send a GET request to get all orders")
    public void iSendAGETRequestToGetAllOrders() {
        response = given()
                .auth().oauth2(storedAccessToken)
                .when()
                .get("/orders/");

        response.prettyPrint();
    }

    @And("the response should contain orders details")
    public void theResponseShouldContainOrdersDetails() {
        response.prettyPrint();
    }

    @Then("I store the first order id")
    public void i_store_the_first_order_id() {
        List<Map<String, Object>> orders = response.jsonPath().getList("");
        Assert.assertFalse(orders.isEmpty(), "Orders list should not be empty");
        storedOrderId = (String) orders.get(0).get("id");
        Assert.assertNotNull(storedOrderId, "Stored order ID should not be null");
    }



    @Then("I send a GET request to get order with selected id")
    public void iSendAGETRequestToGetOrderWithSelectedId() {
        response = given()
                .auth().oauth2(storedAccessToken)
                .when()
                .get("/orders/" + storedOrderId);
        response.prettyPrint();

    }

    @When("I send a {string} request to {string} the order")
    public void i_send_a_request_to_the_order(String requestType, String string2) {


            switch (requestType.toLowerCase()) {
                case "patch":
                    String body = "";
                    response = given()
                            .auth().oauth2(storedAccessToken)
                            .contentType("application/json")
                            .body(body)
                            .when()
                            .patch("/orders/" + storedOrderId);
                    break;
                case "delete":
                    response = given()
                            .auth().oauth2(storedAccessToken)
                            .when()
                            .delete("/orders/" + storedOrderId);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported request type: " + requestType);

            }

    }




}



