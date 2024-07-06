package com.sohbet.exception.message;

import org.springframework.stereotype.Service;

import com.sohbet.exception.ResourceForbiddenException;

@Service
public class ResourceService {

    public void getResource(String resourceId) {
        // Simulate a forbidden resource access
        throw new ResourceForbiddenException("Access to resource " + resourceId + " is forbidden");
    }
}
