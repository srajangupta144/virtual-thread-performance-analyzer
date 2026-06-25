package platformAndVirtualThreadAnalyzer.threadAnalyzer.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkRequest {

    @NotNull(message = "Thread mode is required")
    private String threadMode; // PLATFORM, VIRTUAL

    @NotNull(message = "Workload type is required")
    private String workloadType; // CPU, IO, MIXED

    @Min(value = 1, message = "Concurrent users must be at least 1")
    @Max(value = 100000, message = "Concurrent users exceeds limit")
    private int concurrentUsers;

    @Min(value = 1, message = "Duration must be at least 1 second")
    @Max(value = 3600, message = "Duration cannot exceed 1 hour")
    private int durationInSeconds;

    private boolean pinningEnabled;
}
