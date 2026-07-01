package platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.model.BenchmarkMode;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenchmarkMetricsService {

    private final MeterRegistry meterRegistry;

    private final Map<BenchmarkMode, Counter> requestCounters  = new EnumMap<>(BenchmarkMode.class);
    private final Map<BenchmarkMode, Counter> errorCounters    = new EnumMap<>(BenchmarkMode.class);
    private final Map<BenchmarkMode, Timer>   executionTimers  = new EnumMap<>(BenchmarkMode.class);

    private Counter pinnedThreadsCounter;

    @PostConstruct
    public void init() {
        for (BenchmarkMode mode : BenchmarkMode.values()) {
            requestCounters.put(mode,
                    Counter.builder("benchmark.requests.total")
                            .description("Total benchmark requests")
                            .tag("mode", mode.name())
                            .register(meterRegistry));

            errorCounters.put(mode,
                    Counter.builder("benchmark.errors.total")
                            .description("Total benchmark errors")
                            .tag("mode", mode.name())
                            .register(meterRegistry));

            executionTimers.put(mode,
                    Timer.builder("benchmark.execution.time")
                            .description("Benchmark execution duration")
                            .tag("mode", mode.name())
                            .register(meterRegistry));
        }

        pinnedThreadsCounter = Counter.builder("benchmark.pinned.threads.total")
                .description("Total pinned benchmark requests")
                .register(meterRegistry);

        log.info("Benchmark metrics pre-registered for all modes");
    }

    public void incrementBenchmarkRequests(BenchmarkMode mode) {
        requestCounters.get(mode).increment();
    }

    public void incrementBenchmarkErrors(BenchmarkMode mode) {
        errorCounters.get(mode).increment();
    }

    public void recordExecutionTime(BenchmarkMode mode, long durationMs) {
        executionTimers.get(mode).record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void incrementPinnedThreads() {
        pinnedThreadsCounter.increment();
    }
}