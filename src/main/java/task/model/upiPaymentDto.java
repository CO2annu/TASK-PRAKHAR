package task.model;

import jakarta.validation.Valid;
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
public class upiPaymentDto {
	@NotNull
	@Min(value = 1, message = "Value cant be zero")
	@Max(value = 1000000000, message = "cant exceed integer range")
	private Integer amount;
	
	@NotNull
	@Size(min = 3, max=3, message = "currency can only be of size 3")
	private String currency;
	
	@NotNull
	@Email(message = " kindly enter valid email")
	private String email;
	
	@NotNull
	@Size(min = 10)
	private String contact;
	
	private String order_id;
	private String method = "upi";
	
	@NotNull
	@Valid
	private UpiDto  upi;
	
	
}
