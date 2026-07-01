package platformAndVirtualThreadAnalyzer.threadAnalyzer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.dto.BenchmarkResponse;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.service.BenchmarkService;

@RestController
@RequestMapping("/api/benchmark")
@RequiredArgsConstructor
public class BenchmarkController {
    private final BenchmarkService benchmarkService;
    @GetMapping("/platform/io")
    public ResponseEntity<BenchmarkResponse> runPlatformIO(){
        return ResponseEntity.ok(benchmarkService.executePlatformIoBenchmark());
    }
    @GetMapping("/virtual/io")
    public ResponseEntity<BenchmarkResponse> runVirtualIO(){
        return ResponseEntity.ok(benchmarkService.executeVirtualIoBenchmark());
    }

    @GetMapping("/virtual/pinning")
    public ResponseEntity<BenchmarkResponse> runPinnedBenchmark(){
        return ResponseEntity.ok(benchmarkService.executePinnedBenchmark());
    }




}
