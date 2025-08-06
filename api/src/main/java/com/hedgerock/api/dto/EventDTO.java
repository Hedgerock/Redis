package com.hedgerock.api.dto;

import java.io.Serializable;

public record EventDTO(String id, String title, String description) implements Serializable {
}
