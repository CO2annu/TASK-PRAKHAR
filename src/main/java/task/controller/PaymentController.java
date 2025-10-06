package task.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import task.model.OrderRequestDto;
import task.model.Transaction;
import task.model.cardPaymentDto;
import task.model.orderResponseDto;
import task.model.upiPaymentDto;
import task.repository.paymentTransactionRepository;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	private final WebClient razorPay;	
	private final paymentTransactionRepository transactionRepo;
	public PaymentController(WebClient razorPay, paymentTransactionRepository transactionRepo) {
		this.razorPay = razorPay;
		this.transactionRepo = transactionRepo;
	}
	
	@GetMapping("/{id}")
	public Mono<String> getPayemntDetails(@PathVariable String id){	
		return razorPay.get()
		.uri("/payments/{id}", id)
		.retrieve()
		.bodyToMono(String.class);
	}
	
	@PostMapping("/card")
	public Mono<ResponseEntity<String>> doCardPayment(@RequestBody @Valid cardPaymentDto cardDto){
		OrderRequestDto order = new OrderRequestDto();
		order.setAmount(cardDto.getAmount());
		order.setCurrency(cardDto.getCurrency());
		return razorPay.post()
		.uri("/orders")
		.bodyValue(order)
		.retrieve()
		.bodyToMono(orderResponseDto.class)
		.flatMap(newOrder -> {
			//System.out.println(newOrder.getId());
			cardDto.setOrder_id(newOrder.getId());
			return razorPay.post()
					.uri("/payments/create")
					.bodyValue(cardDto)
					.retrieve()
					.bodyToMono(String.class)
					.flatMap(html -> {
						 Matcher matcher = Pattern.compile("payment_id: '([^']+)'").matcher(html);
		                    if (!matcher.find()) {
		                        //System.err.println(">>> Could not find payment_id in HTML response!");
		                        return Mono.just(ResponseEntity
		                        		.status(HttpStatus.EXPECTATION_FAILED)
		                        		.body(new String("Can't get payment id")));
		                    }
		                    String payId = matcher.group(1);
		                    System.out.println(payId);
		                    Transaction tx = new Transaction(
		                    		null,
		                    		payId,
		                    		cardDto.getOrder_id(),
		                    		"created",
		                    		cardDto.getAmount(),
		                    		null,
		                    		null
		                    		);
		                    
		                    	return transactionRepo.save(tx)
		                    			.thenReturn(ResponseEntity
		                    					.status(HttpStatus.CREATED)
		                    					.body(html));
					});
		});
	}
	
	@PostMapping("/upi")
	public Mono<ResponseEntity<String>> doUpiPayment(@RequestBody @Valid upiPaymentDto upiDto){
		OrderRequestDto order = new OrderRequestDto();
		order.setAmount(upiDto.getAmount());
		order.setCurrency(upiDto.getCurrency());
		//System.out.print(order);
		return razorPay.post()
				.uri("/orders")
				.bodyValue(order)
				.retrieve()
				.bodyToMono(orderResponseDto.class)
				.flatMap(neworder ->{
					upiDto.setOrder_id(neworder.getId());
					return razorPay.post()
							.uri("/payments/create")
							.bodyValue(upiDto)
							.retrieve()
							.bodyToMono(String.class)
							.flatMap(html ->{
								Pattern pattern = Pattern.compile("\"payment_id\"\\s*:\\s*\"([^\"]+)\"");
			                	Matcher matcher = pattern.matcher(html);
			                	if(!matcher.find()) {
			                		return Mono.just(ResponseEntity
			                				.status(HttpStatus.EXPECTATION_FAILED)
			                				.body(new String("cant get payment Id")));
			                	}
			                	String payId = matcher.group(1);
			                	Transaction tx = new Transaction(
			                			null,
			                			payId,
			                			upiDto.getOrder_id(),
			                			"created",
			                			upiDto.getAmount(),
			                			null,
			                			null
			                			);
			                	return transactionRepo.save(tx)
			                	.thenReturn(ResponseEntity
			                			.status(HttpStatus.CREATED)
			                			.body(html));
							});
				});
	}
}
