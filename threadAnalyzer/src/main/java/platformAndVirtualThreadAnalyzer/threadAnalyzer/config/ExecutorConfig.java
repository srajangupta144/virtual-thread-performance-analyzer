package platformAndVirtualThreadAnalyzer.threadAnalyzer.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

    private final BenchmarkProperties properties;

    @Bean(name = "platformExecutor")
    public ExecutorService platformExecutor() {

        return Executors.newFixedThreadPool(
                properties.getPlatformThreadPoolSize()
        );
    }
}
