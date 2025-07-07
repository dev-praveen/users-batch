package com.praveen.batch.listener;

import com.praveen.batch.model.UserCsvRecord;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReaderListener implements ItemReadListener<UserCsvRecord> {

  private int count = 0;

  @Override
  public void afterRead(@Nonnull UserCsvRecord item) {

    count++;
    int chunkSize = 100;
    if (count % chunkSize == 0) {
      log.info("Read {} items so far", count);
    }
  }


}
