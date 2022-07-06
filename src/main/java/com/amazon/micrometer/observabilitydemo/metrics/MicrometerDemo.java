package com.amazon.micrometer.observabilitydemo.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class MicrometerDemo {

    private Counter taskCounter;

    /**
     * Metrics starting with 'jvm' will be ignored
     * @return
     */
    @Bean
    MeterFilter meterFilter() { return MeterFilter.denyNameStartsWith("jvm"); }

    /**
     * Binders register one or more metrics to provide information about the state of
     * some aspect of the application or its container.
     *
     * @return
     */
    @Bean
    MeterBinder meterBinder() {
        return new MeterBinder() {
            @Override
            public void bindTo(MeterRegistry meterRegistry) {
                taskCounter = Counter.builder("micrometer.fixed.rate.tasks")
                            .tag("type", "counter")
                            .description("The number of fixed rate tasks")
                            .baseUnit("tasks")
                            .register(meterRegistry);
            }
        };
    }

    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() {
        taskCounter.increment();
        System.out.println("Fixed rate task count - " + taskCounter.count());
    }

}
