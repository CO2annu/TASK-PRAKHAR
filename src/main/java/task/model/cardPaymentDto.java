package task.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cardPaymentDto {
	@NotNull
	@Min(value = 1, message = "amount must be greater than 0")
	@Max(value = 10000000, message = "amount must not exceed 10,000,000")
	private Integer amount;
	
	@NotNull
	@Size(min = 3, max=3, message = " kindly share ISO 4217 standard currency format")
	private String currency;
	
	@NotNull
	@Email(message = "provide valid email")
	private String email;
	
	@NotNull
	@Size(max = 10, message = "provide valid number")
	private String contact;
	
	private String order_id;
	
	private String method = "card";
	
	@NotNull
	private CardDto card;
	
}
