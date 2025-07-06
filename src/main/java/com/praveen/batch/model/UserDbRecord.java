package com.praveen.batch.model;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record UserDbRecord(
    String firstName,
    String lastName,
    String fullName,
    String sex,
    String email,
    String phone,
    LocalDate dateOfBirth,
    String jobTitle) {}
