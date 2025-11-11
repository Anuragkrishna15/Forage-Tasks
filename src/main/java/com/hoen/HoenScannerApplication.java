package com.hoen;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    private List<SearchResult> searchResults;

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Hoen Scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final HoenScannerConfiguration configuration,
                    final Environment environment) {
        // Load JSON files
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream rentalCarsStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json");
            List<SearchResult> rentalCars = mapper.readValue(rentalCarsStream, new TypeReference<List<SearchResult>>() {});

            InputStream hotelsStream = getClass().getClassLoader().getResourceAsStream("hotels.json");
            List<SearchResult> hotels = mapper.readValue(hotelsStream, new TypeReference<List<SearchResult>>() {});

            searchResults = new java.util.ArrayList<>();
            searchResults.addAll(rentalCars);
            searchResults.addAll(hotels);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load search results", e);
        }

        // Register resources
        environment.jersey().register(new SearchResource(searchResults));
    }
}