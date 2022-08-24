import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class httpApiTests {

        private static TasksAppServer appServer;

        /**
         * Start the app server before all tests run
         */
        @BeforeAll
        public static void startServer() {
        appServer = new TasksAppServer();
        appServer.start(5000);
    }

        /**
         * Stop the app server once all tests are run
         */
        @AfterAll
        public static void stopServer() {
        appServer.stop();
    }

        /**
         * Test `GET /tasks`.
         *
         */
        @Test
        public void getAllTasks() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5000/tasks").asJson();
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        JSONArray jsonArray = response.getBody().getArray();
        assertTrue(jsonArray.length() > 1);
    }

        /**
         * Test for
         *
         */
        @Test
//    @Disabled("Incomplete")
        public void getOneTask() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5000/task/1").asJson();
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        JSONObject jsonObject = response.getBody().getObject();
        assertEquals(1, jsonObject.getInt("id"));
        assertEquals("Buy milk", jsonObject.get("description"));
    }

        /**
         * Test
         *
         */
        @Test
//    @Disabled("Incomplete")
        public void taskNotFound() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5000/task/0").asJson();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

        /**
         * Test for 1.3
         * Remember to remove the @Disabled annotation
         */
        @Test
//    @Disabled("Incomplete")
        void addTask() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5000/task")
                .header("Content-Type", "application/json")
                .body(new Task(100, "Water the plants"))
                .asJson();
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("/task/100", response.getHeaders().getFirst("Location"));

        response = Unirest.get("http://localhost:5000/task/100").asJson();
        assertEquals(HttpStatus.OK, response.getStatus());
    }

        /**
         * Test for 1.4
         * Remember to remove the @Disabled annotation
         */
        @Test
//    @Disabled("Incomplete")
        void duplicateTask() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5000/task")
                .header("Content-Type", "application/json")
                .body(new Task(1, "Water the plants"))
                .asJson();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }
    }

