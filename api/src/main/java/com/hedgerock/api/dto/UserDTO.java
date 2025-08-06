package com.hedgerock.api.dto;

import java.io.Serializable;
import java.util.Set;

public record UserDTO(String id, String name, int age, Set<String >events) implements Serializable {
}
