package com.praveen.batch.listener;

import com.praveen.batch.model.UserCsvRecord;
import com.praveen.batch.model.UserDbRecord;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessorListener implements ItemProcessListener<UserCsvRecord, UserDbRecord> {

  private int count = 0;

  @Override
  public void afterProcess(@Nonnull UserCsvRecord item, UserDbRecord result) {
    count++;
    int chunkSize = 100;
    if (count % chunkSize == 0) {
      log.info("Processed {} items so far", count);
    }
  }
}
