package platformAndVirtualThreadAnalyzer.threadAnalyzer.service;

import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.controller.Benchmark;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.dto.BenchmarkResponse;

public interface BenchmarkService {
    BenchmarkResponse executePlatformIoBenchmark();

    BenchmarkResponse executeVirtualIoBenchmark();

    BenchmarkResponse executePinnedBenchmark();
}

//Benchmarking is the process of measuring and evaluating the performance
// of a system, application, or component under specific conditions.