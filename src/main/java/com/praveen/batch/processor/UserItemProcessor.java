package com.praveen.batch.processor;

import com.praveen.batch.model.Gender;
import com.praveen.batch.model.UserDbRecord;
import com.praveen.batch.model.UserCsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import java.time.LocalDate;

@Slf4j
public class UserItemProcessor implements ItemProcessor<UserCsvRecord, UserDbRecord> {

  @Override
  public UserDbRecord process(UserCsvRecord userCsvRecord) {

    return UserDbRecord.builder()
        .firstName(userCsvRecord.firstName())
        .lastName(userCsvRecord.lastName())
        .fullName(userCsvRecord.firstName().concat(" ").concat(userCsvRecord.lastName()))
        .sex(Gender.valueOf(userCsvRecord.sex().toUpperCase()).getValue())
        .email(userCsvRecord.email())
        .dateOfBirth(LocalDate.parse(userCsvRecord.dateOfBirth()))
        .jobTitle(userCsvRecord.jobTitle())
        .phone(userCsvRecord.phone())
        .build();
  }
}
