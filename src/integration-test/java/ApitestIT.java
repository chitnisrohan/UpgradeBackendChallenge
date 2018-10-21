import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

public class ApitestIT implements Runnable {

    @BeforeClass
    public static void setUp() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        }
        else{
            RestAssured.port = Integer.valueOf(port);
        }


        String basePath = System.getProperty("server.base");
        if(basePath==null){
            basePath = "/wallet/";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if(baseHost==null){
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;

        Map<String, String> map = new HashMap<>();
        map.put("firstName", "John");
        map.put("lastName", "Doe");
        map.put("address", "121 Main Street");
        map.put("phone", "879-984-8989");
        given()
                .contentType("application/json")
                .body(map)
                .when().post("/createUser").then()
                .statusCode(200);

        map = new HashMap<>();
        map.put("branch", "Boston");
        map.put("balance", "123456");
        map.put("accountType", "TRANSACTION");
        given()
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post("/createWallet/1").then()
                .statusCode(200);

    }

    @Test
    public void testGetBalance(){
        testBalance((float)123456.0);
    }

    private void testBalance(Float balanceToVerify) {
        expect().statusCode(200).contentType(ContentType.JSON).when()
                .get("/getBalance/2");
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/getBalance/2");
        JsonPath jsonPathEvaluator = response.jsonPath();
        Float balance = jsonPathEvaluator.get("balance");
        assertEquals(balanceToVerify, balance);
    }

    @Test
    public void testWithdrawAndDeposit() {
        expect().statusCode(200).contentType(ContentType.JSON).when()
                .put("/withdraw/2/100");
        testBalance((float) 123356.0);
        expect().statusCode(200).contentType(ContentType.JSON).when()
                .put("/deposit/2/100");
        testBalance((float)123456.0);
    }

    @Test
    public void testReverse() {
        expect().statusCode(500).contentType(ContentType.JSON).when()
                .put("/reverse/00");
//        expect().statusCode(200).contentType(ContentType.JSON).when()
//                .put("/reverse/f8f7b834-9b43-46e1-8e8b-f999a59c84f4");
    }

    @Test
    public void testGetNTransactions() {
        expect().statusCode(200).contentType(ContentType.JSON).when()
                .get("/getNTransactions/2/5");

    }

    @Test
    public void testConcurrency() {
        Thread[] threads = new Thread[10];
        for (int i = 0 ; i < 10; i++) {
            threads[i] = new Thread(this);
        }
        for (int i = 0 ; i < 10; i++) {
            threads[i].start();
        }

        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/getBalance/2");
        JsonPath jsonPathEvaluator = response.jsonPath();
        Float balance = jsonPathEvaluator.get("balance");
        //assertEquals(123456.0, balance,0);
    }

    @Override
    public void run() {
        testWithdraw();
        testDeposit();
    }

    private void testDeposit() {
        expect().statusCode(200).contentType(ContentType.JSON).when()
                .put("/deposit/2/100");
    }

    private void testWithdraw() {
        expect().statusCode(200).contentType(ContentType.JSON).when()
                .put("/withdraw/2/100");
    }
}
