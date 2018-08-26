package com.jrmcdonald.padx.common;

import java.io.IOException;
import java.nio.file.Files;


import static org.assertj.core.api.Assertions.fail;

import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base test class designed to set the "unit-test" profile.
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@ActiveProfiles("unit-test")
public class BaseTest {

    /**
     * Read in a resource and return the contents as a string.
     * 
     * @param resouce the resource to read
     * @return a string representation of the resouce
     */
    protected String getResourceAsString(Resource resource) {
        String value = null;

        try {
            value = new String(Files.readAllBytes((resource.getFile()).toPath()));
        } catch (IOException ex) {
            ex.printStackTrace();
            fail("An exception occurred reading a resource: " + resource.toString());
        }

        return value;
    }
}