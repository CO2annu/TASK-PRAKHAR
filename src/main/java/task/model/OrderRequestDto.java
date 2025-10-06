package task.model;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
	@NotNull
	private Integer amount;
	
	@NotNull
	@Size(min = 3, message = "currency length must be length 3")
	private String currency;
	
	private String receipt;
	
	private Map<String, String> notes;
}
