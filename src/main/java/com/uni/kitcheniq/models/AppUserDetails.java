package com.uni.kitcheniq.models;

import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserDetails extends UserDetails {
    String getId();
    String getName();
}
