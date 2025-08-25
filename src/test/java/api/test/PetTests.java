package api.test;

import api.endpoints.PetEndpoints;
import api.payload.Pet;
import baseTest.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PetTests extends BaseTest {

    private Faker faker;
    private Pet petPayload;
    private String[] statuses = {"available", "pending", "sold"};

    @BeforeClass
    public void setupData() {
        faker = new Faker();
        petPayload = new Pet();

        petPayload.setId(faker.number().numberBetween(1000, 10000));
        petPayload.setName(faker.name().firstName());
        petPayload.setStatus(statuses[faker.number().numberBetween(0, statuses.length)]);

        Pet.Category category = new Pet.Category();
        category.setId(faker.number().numberBetween(1, 10));
        category.setName(faker.animal().name());
        petPayload.setCategory(category);

        List<String> photos = new ArrayList<>();
        photos.add(faker.internet().url());
        photos.add(faker.internet().url());
        petPayload.setPhotoUrls(photos);

        List<Pet.Tag> tags = new ArrayList<>();
        Pet.Tag t1 = new Pet.Tag();
        t1.setId(faker.number().randomDigit());
        t1.setName(faker.color().name());
        Pet.Tag t2 = new Pet.Tag();
        t2.setId(faker.number().randomDigit());
        t2.setName(faker.book().genre());
        tags.add(t1);
        tags.add(t2);
        petPayload.setTags(tags);

        logInfo("Test data setup completed for Pet ID: " + petPayload.getId());
    }

    @Test(priority = 1)
    public void testPostPet() {
        Response response = PetEndpoints.createPet(petPayload);
        response.then().log().all();
        logInfo("POST Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2, dependsOnMethods = {"testPostPet"})
    public void testGetPet() throws InterruptedException {
        Response response = retryPetOperation(petPayload.getId(), "GET", petPayload);
        response.then().log().all();
        logInfo("GET Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("name"), petPayload.getName());
    }

    @Test(priority = 3, dependsOnMethods = {"testPostPet"})
    public void testUpdatePet() throws InterruptedException {
        petPayload.setName(faker.name().firstName());
        petPayload.setStatus(statuses[faker.number().numberBetween(0, statuses.length)]);

        Response response = PetEndpoints.updatePet(petPayload);
        response.then().log().all();
        logInfo("UPDATE Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        Response getResponse = retryPetOperation(petPayload.getId(), "GET", petPayload);
        getResponse.then().log().all();
        logInfo("GET after UPDATE Pet response: " + getResponse.getBody().asString());
        Assert.assertEquals(getResponse.jsonPath().getString("status"), petPayload.getStatus());
    }

    @Test(priority = 4, dependsOnMethods = {"testGetPet"})
    public void testDeletePet() throws InterruptedException {
        Response response = retryPetOperation(petPayload.getId(), "DELETE", petPayload);
        response.then().log().all();
        logInfo("DELETE Pet response: " + response.getBody().asString());
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 404);
    }
}
