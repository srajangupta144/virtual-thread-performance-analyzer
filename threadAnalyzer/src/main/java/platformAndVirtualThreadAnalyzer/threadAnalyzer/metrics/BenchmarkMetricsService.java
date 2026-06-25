package platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.model.BenchmarkMode;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BenchmarkMetricsService {

    private final MeterRegistry meterRegistry;

    public void incrementBenchmarkRequests(BenchmarkMode mode) {

        Counter.builder("benchmark.requests.total")
                .description("Total benchmark requests")
                .tag("mode", mode.name())
                .register(meterRegistry)
                .increment();
    }

    public void incrementBenchmarkErrors(BenchmarkMode mode) {

        Counter.builder("benchmark.errors.total")
                .description("Total benchmark errors")
                .tag("mode", mode.name())
                .register(meterRegistry)
                .increment();
    }

    public void incrementPinnedThreads() {

        Counter.builder("benchmark.pinned.threads.total")
                .description("Total pinned virtual threads")
                .register(meterRegistry)
                .increment();
    }

    public void recordExecutionTime(BenchmarkMode mode,
                                    long durationMs) {

        Timer.builder("benchmark.execution.time")
                .description("Benchmark execution duration")
                .tag("mode", mode.name())
                .register(meterRegistry)
                .record(durationMs, TimeUnit.MILLISECONDS);
    }
}