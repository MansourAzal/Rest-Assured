package api.endpoints;

import api.utilities.ConfigManager;

public class Routes {

    // Base URL read from config.properties
    private static final String BASE_URL = ConfigManager.getBaseUrl();

    // Pet endpoints read from config.properties
    public static final String POST_PET   = BASE_URL + ConfigManager.getCreatePetEndpoint();
    public static final String GET_PET    = BASE_URL + ConfigManager.getGetPetEndpoint();
    public static final String UPDATE_PET = BASE_URL + ConfigManager.getUpdatePetEndpoint();
    public static final String DELETE_PET = BASE_URL + ConfigManager.getDeletePetEndpoint();

    // You can add more endpoints here for other resources
    // Example:
    // public static final String USER_URL = BASE_URL + ConfigManager.getUserEndpoint();
}
