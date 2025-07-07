package com.praveen.batch.listener;

import com.praveen.batch.model.UserDbRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WriterListener implements ItemWriteListener<UserDbRecord> {

  @Override
  public void afterWrite(Chunk<? extends UserDbRecord> items) {

    int count = items.size();
    log.info("Written {} items so far in database", count);
  }
}
