package com.hedgerock.api.dto;

import java.util.Set;

public record UserDTO(String id, String name, int age, Set<String >events) {
}
