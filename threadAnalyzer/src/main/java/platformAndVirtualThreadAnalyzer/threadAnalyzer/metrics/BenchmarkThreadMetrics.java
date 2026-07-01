package platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BenchmarkThreadMetrics {

    private final AtomicInteger platformThreads = new AtomicInteger();
    private final AtomicInteger virtualThreads = new AtomicInteger();
    private final AtomicInteger pinnedThreads = new AtomicInteger();

    public BenchmarkThreadMetrics(MeterRegistry registry) {

        Gauge.builder("benchmark.inflight.tasks",
                        platformThreads,
                        AtomicInteger::get)
                .tag("mode", "PLATFORM")
                .register(registry);

        Gauge.builder("benchmark.inflight.tasks",
                        virtualThreads,
                        AtomicInteger::get)
                .tag("mode", "VIRTUAL")
                .register(registry);

        Gauge.builder("benchmark.inflight.tasks",
                        pinnedThreads,
                        AtomicInteger::get)
                .tag("mode", "VIRTUAL_PINNED")
                .register(registry);
    }

    public AtomicInteger getPlatformThreads() {
        return platformThreads;
    }

    public AtomicInteger getVirtualThreads() {
        return virtualThreads;
    }

    public AtomicInteger getPinnedThreads() {
        return pinnedThreads;
    }
}