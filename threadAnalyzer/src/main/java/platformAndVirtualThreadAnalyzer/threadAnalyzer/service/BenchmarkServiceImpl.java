package platformAndVirtualThreadAnalyzer.threadAnalyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.dto.BenchmarkResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenchmarkServiceImpl implements BenchmarkService{          //implementation is diff as in future we can change the implementation without changing the controller
    private final VirtualBenchmarkService virtualService;
    private final PlatformBenchmarkService platformService;

    @Override
    public BenchmarkResponse executePlatformIoBenchmark() {
        log.info("Executing platform io benchmark");
        return platformService.runIoBenchmark();
    }

    @Override
    public BenchmarkResponse executeVirtualIoBenchmark() {
        log.info("Executing virtual io benchmark");
        return virtualService.runIoBenchmark();
    }

    @Override
    public BenchmarkResponse executePinnedBenchmark() {
        log.info("Executing pinned benchmark");
        return virtualService.runPinnedBenchmark();
    }
}
