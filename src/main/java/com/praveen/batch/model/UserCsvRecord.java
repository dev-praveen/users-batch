package com.praveen.batch.model;

import lombok.Builder;

@Builder
public record UserCsvRecord(
    String firstName,
    String lastName,
    String sex,
    String email,
    String phone,
    String dateOfBirth,
    String jobTitle) {}
