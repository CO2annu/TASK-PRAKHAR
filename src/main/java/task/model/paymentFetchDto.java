package task.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class paymentFetchDto {
	private String status;
	private String refund_status;
}
