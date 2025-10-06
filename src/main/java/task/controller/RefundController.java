package task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import task.model.ApiModelWrapper;
import task.model.ErrorDto;
import task.model.RefundResponseDto;
import task.model.refundRequestDto;
import task.repository.paymentTransactionRepository;

@RestController
@RequestMapping("/refund")
public class RefundController {
	
	private final WebClient razorPay;
	private final paymentTransactionRepository transactionRepo;
	
	
	
	public RefundController(WebClient razorPay, paymentTransactionRepository transactionRepo) {
		this.razorPay = razorPay;
		this.transactionRepo = transactionRepo;
	}
	
	@PostMapping("/{id}")
	public Mono<ResponseEntity<ApiModelWrapper>> getRefund(@PathVariable String id) {
	    return transactionRepo.findById(id)
	        .flatMap(tuple -> {
	            if (!"captured".equalsIgnoreCase(tuple.getPayment_status())) {
	                return Mono.just(ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)
	                    .body(new ApiModelWrapper(new ErrorDto("Refund only available to captured payments"), null)));
	            }

	            return razorPay.post()
	                .uri("/payments/{id}/refund", tuple.getPayment_id())
	                .bodyValue(new refundRequestDto(tuple.getAmount()))
	                .retrieve()
	                .bodyToMono(RefundResponseDto.class)
	                .flatMap(refundresponse -> {
	                    tuple.setRefund_id(refundresponse.getId());
	                    tuple.setRefund_status(refundresponse.getStatus());
	                    return transactionRepo.save(tuple)
	                        .map(saved -> ResponseEntity.status(HttpStatus.OK).body(new ApiModelWrapper(null, refundresponse)));
	                });
	        })
	        .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiModelWrapper(new ErrorDto("Id not found"), null))));
	        
	}
}
