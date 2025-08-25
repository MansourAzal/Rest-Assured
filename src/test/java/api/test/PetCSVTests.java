package api.test;

import api.endpoints.PetEndpoints;
import api.payload.Pet;
import api.utilities.CSVUtility;
import baseTest.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PetCSVTests extends BaseTest {

    private static final String CSV_PATH = "src/test/resources/testData/Petdata.csv";

    @DataProvider(name = "petData")
    public Object[][] petDataProvider() {
        return CSVUtility.getCSVData(CSV_PATH);
    }

    // Helper to build Pet object
    private Pet buildPet(String id, String name, String status, String categoryId, String categoryName,
                         String photo1, String photo2, String tag1Id, String tag1Name, String tag2Id, String tag2Name) {

        Pet pet = new Pet();
        pet.setId(Integer.parseInt(id));
        pet.setName(name);
        pet.setStatus(status);

        Pet.Category category = new Pet.Category();
        category.setId(Integer.parseInt(categoryId));
        category.setName(categoryName);
        pet.setCategory(category);

        List<String> photos = new ArrayList<>();
        photos.add(photo1);
        photos.add(photo2);
        pet.setPhotoUrls(photos);

        List<Pet.Tag> tags = new ArrayList<>();
        Pet.Tag t1 = new Pet.Tag();
        t1.setId(Integer.parseInt(tag1Id));
        t1.setName(tag1Name);

        Pet.Tag t2 = new Pet.Tag();
        t2.setId(Integer.parseInt(tag2Id));
        t2.setName(tag2Name);

        tags.add(t1);
        tags.add(t2);
        pet.setTags(tags);

        return pet;
    }

    @Test(priority = 1, dataProvider = "petData")
    public void testPostPet(String id, String name, String status, String categoryId, String categoryName,
                            String photo1, String photo2, String tag1Id, String tag1Name, String tag2Id, String tag2Name) {

        Pet pet = buildPet(id, name, status, categoryId, categoryName, photo1, photo2, tag1Id, tag1Name, tag2Id, tag2Name);
        Response response = PetEndpoints.createPet(pet);
        response.then().log().all();
        logInfo("POST Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2, dependsOnMethods = {"testPostPet"}, dataProvider = "petData")
    public void testGetPet(String id, String name, String status, String categoryId, String categoryName,
                           String photo1, String photo2, String tag1Id, String tag1Name, String tag2Id, String tag2Name) throws InterruptedException {

        Pet pet = buildPet(id, name, status, categoryId, categoryName, photo1, photo2, tag1Id, tag1Name, tag2Id, tag2Name);
        Response response = retryPetOperation(pet.getId(), "GET", pet);
        response.then().log().all();
        logInfo("GET Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        String actualName = response.jsonPath().getString("name");
        Assert.assertTrue(actualName.equals(name) || actualName.equals(name + "_updated"),
                "Pet name mismatch! Expected: " + name + " or " + name + "_updated, Actual: " + actualName);
    }

    @Test(priority = 3, dependsOnMethods = {"testPostPet"}, dataProvider = "petData")
    public void testUpdatePet(String id, String name, String status, String categoryId, String categoryName,
                              String photo1, String photo2, String tag1Id, String tag1Name, String tag2Id, String tag2Name) {

        Pet pet = buildPet(id, name + "_updated", status.equals("available") ? "pending" : "available",
                categoryId, categoryName, photo1, photo2, tag1Id, tag1Name, tag2Id, tag2Name);

        Response response = PetEndpoints.updatePet(pet);
        response.then().log().all();
        logInfo("UPDATE Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4, dependsOnMethods = {"testGetPet"}, dataProvider = "petData")
    public void testDeletePet(String id, String name, String status, String categoryId, String categoryName,
                              String photo1, String photo2, String tag1Id, String tag1Name, String tag2Id, String tag2Name) throws InterruptedException {

        Pet pet = buildPet(id, name, status, categoryId, categoryName, photo1, photo2, tag1Id, tag1Name, tag2Id, tag2Name);
        Response response = retryPetOperation(pet.getId(), "DELETE", pet);
        response.then().log().all();
        logInfo("DELETE Pet response: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
