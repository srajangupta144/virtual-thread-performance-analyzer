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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class VirtualBenchmarkService {

    private final Object lock = new Object();

    private final BenchmarkMetricsService metricsService;

    @Qualifier("virtualExecutor")
    private final ExecutorService executor;

    public BenchmarkResponse runIoBenchmark() {

        long start = System.currentTimeMillis();

        try {

            Future<String> future = executor.submit(() -> {

                Thread.sleep(100);

                return "SUCCESS";
            });

            future.get();

            long duration = System.currentTimeMillis() - start;

            metricsService.incrementBenchmarkRequests(
                    BenchmarkMode.VIRTUAL);

            metricsService.recordExecutionTime(
                    BenchmarkMode.VIRTUAL,
                    duration
            );

            log.info("Virtual benchmark completed in {} ms",
                    duration);

            return BenchmarkResponse.builder()
                    .mode(BenchmarkMode.VIRTUAL)
                    .executionTime(duration)
                    .status("SUCCESS")
                    .build();

        } catch (Exception ex) {

            metricsService.incrementBenchmarkRequests(
                    BenchmarkMode.VIRTUAL);

            throw new BenchmarkException(
                    "Virtual benchmark failed", ex);
        }
    }

    public BenchmarkResponse runPinnedBenchmark() {

        long start = System.currentTimeMillis();

        try {

            executor.submit(() -> {

                synchronized (lock) {

                    metricsService.incrementPinnedThreads();

                    Thread.sleep(100);
                }

                return null;

            }).get();

            long duration = System.currentTimeMillis() - start;

            metricsService.incrementBenchmarkRequests(
                    BenchmarkMode.VIRTUAL_PINNED);

            metricsService.recordExecutionTime(
                    BenchmarkMode.VIRTUAL_PINNED,
                    duration);

            log.info("Pinned benchmark completed in {} ms",
                    duration);

            return BenchmarkResponse.builder()
                    .mode(BenchmarkMode.VIRTUAL_PINNED)
                    .executionTime(duration)
                    .status("SUCCESS")
                    .build();

        } catch (Exception ex) {

            metricsService.incrementBenchmarkErrors(
                    BenchmarkMode.VIRTUAL_PINNED);

            throw new BenchmarkException(
                    "Pinned benchmark failed", ex);
        }
    }
}