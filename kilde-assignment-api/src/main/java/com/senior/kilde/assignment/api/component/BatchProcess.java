package com.senior.kilde.assignment.api.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchProcess {


    @Scheduled(cron = "0 0 0 * * *")
    public void trackOverduePayments() {
        // TODO : might need to process interest
    }

}
