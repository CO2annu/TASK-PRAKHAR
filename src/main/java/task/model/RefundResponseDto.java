package task.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefundResponseDto {
	private Integer amount;
	private String currency;
	private String id;
	private String status;
}
