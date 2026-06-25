package platformAndVirtualThreadAnalyzer.threadAnalyzer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "benchmark")
@Component
@Data
public class BenchmarkProperties {
    private int platformThreadPoolSize;
    private long simulatedIoDelayMs;
}
