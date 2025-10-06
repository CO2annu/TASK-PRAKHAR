package task.model;

import org.hibernate.validator.constraints.CreditCardNumber;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDto {
	@CreditCardNumber
	private String number;
	
	@NotNull
	private String expiry_month;
	@NotNull
	private String expiry_year;
	
	@NotNull
	@Size(min =  3)
	private String cvv;
	
	@NotNull
	private String name;
}
