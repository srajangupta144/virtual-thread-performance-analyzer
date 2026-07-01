package platformAndVirtualThreadAnalyzer.threadAnalyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.config.BenchmarkProperties;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.dto.BenchmarkResponse;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.exceptions.BenchmarkException;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics.BenchmarkMetricsService;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.metrics.BenchmarkThreadMetrics;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.model.BenchmarkMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VirtualBenchmarkService {

    private final BenchmarkMetricsService metricsService;
    private final BenchmarkThreadMetrics threadMetrics;
    private final BenchmarkProperties properties;

    private final Object lock = new Object();

    public BenchmarkResponse runIoBenchmark() {

        long start = System.currentTimeMillis();

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            threadMetrics.getVirtualThreads().incrementAndGet();

            executor.submit(() -> {
                Thread.sleep(properties.getSimulatedIoDelayMs());
                return null;
            }).get();

            long duration = System.currentTimeMillis() - start;

            metricsService.incrementBenchmarkRequests(BenchmarkMode.VIRTUAL);
            metricsService.recordExecutionTime(BenchmarkMode.VIRTUAL, duration);

            return BenchmarkResponse.builder()
                    .mode(BenchmarkMode.VIRTUAL)
                    .status("SUCCESS")
                    .executionTime(duration)
                    .build();

        } catch (Exception ex) {
            metricsService.incrementBenchmarkErrors(BenchmarkMode.VIRTUAL);
            throw new BenchmarkException("Virtual benchmark failed", ex);

        } finally {
            threadMetrics.getVirtualThreads().decrementAndGet();
        }
    }

    public BenchmarkResponse runPinnedBenchmark() {

        long start = System.currentTimeMillis();

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            threadMetrics.getPinnedThreads().incrementAndGet();

            executor.submit(() -> {
                synchronized (lock) {
                    Thread.sleep(properties.getSimulatedIoDelayMs());
                }
                return null;
            }).get();

            long duration = System.currentTimeMillis() - start;

            metricsService.incrementBenchmarkRequests(BenchmarkMode.VIRTUAL_PINNED);
            metricsService.incrementPinnedThreads();
            metricsService.recordExecutionTime(BenchmarkMode.VIRTUAL_PINNED, duration);

            return BenchmarkResponse.builder()
                    .mode(BenchmarkMode.VIRTUAL_PINNED)
                    .status("SUCCESS")
                    .executionTime(duration)
                    .build();

        } catch (Exception ex) {
            metricsService.incrementBenchmarkErrors(BenchmarkMode.VIRTUAL_PINNED);
            throw new BenchmarkException("Pinned benchmark failed", ex);

        } finally {
            threadMetrics.getPinnedThreads().decrementAndGet();
        }
    }
}