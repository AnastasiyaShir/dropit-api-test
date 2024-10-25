package com.demo.dropit_api_test;

import com.demo.dropit_api_test.model.Category;
import com.demo.dropit_api_test.model.Pet;
import com.demo.dropit_api_test.model.Status;
import com.demo.dropit_api_test.model.Tag;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@SpringBootTest
public class PetStoreApiTests {

    @Value("${base-url}")
    private String baseUrl;

    private static final String NEW_PET_NAME = "Jelly";
    private static final String SEARCHED_DOG_NAME = "Puff";
    private static final String PET_ENDPOINT = "/pet";
    private static final String FIND_BY_STATUS_ENDPOINT = PET_ENDPOINT + "/findByStatus";

    @Test
    public void test_createAndUpdatePet() {
        Pet newPet = buildNewPet();

        Pet createdPet = createPet(newPet);
        log.info("Created Pet: {}", createdPet);

        assertPetDetails(createdPet, NEW_PET_NAME, Status.AVAILABLE);

        createdPet.setStatus(Status.SOLD);
        Pet updatedPet = updatePet(createdPet);
        log.info("Updated Pet: {}", updatedPet);

        assertPetDetails(updatedPet, NEW_PET_NAME, Status.SOLD);
    }

    @Test
    public void test_findPetByStatus_available() {
        List<Pet> foundPets = findPetsByStatus(Status.AVAILABLE);

        assertThat("No available pets found", foundPets, not(emptyCollectionOf(Pet.class)));
        assertThat("Found less than 4 available pets", foundPets, hasSize(greaterThanOrEqualTo(4)));

        log.info("{} available pets found", foundPets.size());

        assertThat("Fourth pet's name does not match", foundPets.get(3).getName(), is(SEARCHED_DOG_NAME));
    }

    @Test
    public void test_findPetByStatus_sold() {
        List<Pet> foundPets = findPetsByStatus(Status.SOLD);
        log.info("{} sold pets found", foundPets.size());
        assertThat("No sold pets found", foundPets, not(emptyCollectionOf(Pet.class)));
        assertThat("Not all pets have status SOLD", foundPets, everyItem(hasProperty("status", is(Status.SOLD))));
    }

    private Pet buildNewPet() {
        Pet newPet = new Pet();
        newPet.setCategory(new Category(1, "Dog"));
        newPet.setName(NEW_PET_NAME);
        newPet.setPhotoUrls(List.of("https://example.com/photo1.jpg"));
        newPet.setTags(List.of(new Tag(1, "friendly")));
        newPet.setStatus(Status.AVAILABLE);
        return newPet;
    }

    private void assertPetDetails(Pet pet, String expectedName, Status expectedStatus) {
        assertThat("Pet ID should not be null", pet.getId(), is(notNullValue()));
        assertThat("Pet name should match", pet.getName(), is(expectedName));
        assertThat("Pet status should match", pet.getStatus(), is(expectedStatus));
    }

    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON);
    }

    private Pet createPet(Pet pet) {
        return baseRequest()
                .body(pet)
                .post(PET_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(Pet.class);
    }

    private Pet updatePet(Pet pet) {
        return baseRequest()
                .body(pet)
                .put(PET_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(Pet.class);
    }

    private List<Pet> findPetsByStatus(Status status) {
        return baseRequest()
                .queryParam("status", status.getValue())
                .get(FIND_BY_STATUS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });
    }
}
