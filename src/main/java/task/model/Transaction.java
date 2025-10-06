package task.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("payment_transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	@Id
	private String id;
	@NotNull
	private String payment_id;
	@NotNull
	private String order_id;
	@NotNull
	private String payment_status;
	@NotNull
	private Integer amount;
	
	
	private String refund_id;
	private String refund_status;
}
