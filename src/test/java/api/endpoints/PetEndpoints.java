package api.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import api.payload.Pet;
import static io.restassured.RestAssured.given;

public class PetEndpoints {

    public static Response createPet(Pet payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.POST_PET);
    }

    public static Response getPet(int petId) {
        return given()
                .pathParam("petId", petId)
                .when()
                .get(Routes.GET_PET);
    }

    public static Response updatePet(Pet payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .put(Routes.UPDATE_PET);
    }

    public static Response deletePet(int petId) {
        return given()
                .pathParam("petId", petId)
                .when()
                .delete(Routes.DELETE_PET);
    }
}
