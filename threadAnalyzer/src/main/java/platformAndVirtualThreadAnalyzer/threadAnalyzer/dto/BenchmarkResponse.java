package platformAndVirtualThreadAnalyzer.threadAnalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platformAndVirtualThreadAnalyzer.threadAnalyzer.model.BenchmarkMode;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BenchmarkResponse {
    private BenchmarkMode mode;
    private String status;
    private long executionTime;
}
