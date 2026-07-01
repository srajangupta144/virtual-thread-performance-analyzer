package platformAndVirtualThreadAnalyzer.threadAnalyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.config.BenchmarkProperties;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.dto.BenchmarkResponse;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.exceptions.BenchmarkException;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics.BenchmarkMetricsService;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics.BenchmarkThreadMetrics;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.model.BenchmarkMode;

import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class PlatformBenchmarkService {

    private final BenchmarkMetricsService metricsService;
    private final BenchmarkThreadMetrics threadMetrics;
    private final ExecutorService platformExecutor;
    private final BenchmarkProperties properties;

    public PlatformBenchmarkService(
            BenchmarkMetricsService metricsService,
            BenchmarkThreadMetrics threadMetrics,
            @Qualifier("platformExecutor") ExecutorService platformExecutor,
            BenchmarkProperties properties) {
        this.metricsService = metricsService;
        this.threadMetrics = threadMetrics;
        this.platformExecutor = platformExecutor;
        this.properties = properties;
    }

    public BenchmarkResponse runIoBenchmark() {

        long start = System.currentTimeMillis();

        try {
            threadMetrics.getPlatformThreads().incrementAndGet();

            platformExecutor.submit(() -> {
                Thread.sleep(properties.getSimulatedIoDelayMs());
                return null;
            }).get();

            long duration = System.currentTimeMillis() - start;

            metricsService.incrementBenchmarkRequests(BenchmarkMode.PLATFORM);
            metricsService.recordExecutionTime(BenchmarkMode.PLATFORM, duration);

            return BenchmarkResponse.builder()
                    .mode(BenchmarkMode.PLATFORM)
                    .status("SUCCESS")
                    .executionTime(duration)
                    .build();

        } catch (Exception ex) {
            metricsService.incrementBenchmarkErrors(BenchmarkMode.PLATFORM);
            throw new BenchmarkException("Platform benchmark failed", ex);

        } finally {
            threadMetrics.getPlatformThreads().decrementAndGet();
        }
    }
}