package platformAndVirtualThreadAnalyzer.threadAnalyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.dto.BenchmarkResponse;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.exceptions.BenchmarkException;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics.BenchmarkMetricsService;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.model.BenchmarkMode;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlatformBenchmarkService {

    private final BenchmarkMetricsService metricsService;

    @Qualifier("platformExecutor")
    private final ExecutorService executor;

    public BenchmarkResponse runIoBenchmark() {

        long start = System.currentTimeMillis();

        try {

            executor.submit(() -> {
                Thread.sleep(100);
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
            throw new BenchmarkException(
                    "Platform benchmark failed", ex);
        }
    }
}