package task.controller;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import task.model.paymentFetchDto;
import task.repository.paymentTransactionRepository;

@Component
public class paymentScheduler {
	
	private final paymentTransactionRepository transactionRepo;
	private final WebClient razorPay;
	public paymentScheduler(paymentTransactionRepository transactionRepo, WebClient razorPay) {
		this.transactionRepo = transactionRepo;
		this.razorPay = razorPay;
	}
	private static final Set<String> FINAL_STATUSES = Set.of(
	        "failed", "refunded"
	    );
	@Scheduled(fixedRate = 5000)
	public void paymentStatusEnquiry() {
		transactionRepo.findAll().filter(tx -> !FINAL_STATUSES.contains(tx.getPayment_status()))
				.flatMap(tuple -> razorPay.get()
									.uri(("/payments/{id}"), tuple.getPayment_id())
									.retrieve()
									.bodyToMono(paymentFetchDto.class)
									.flatMap(newStatus ->{
										if(newStatus.getStatus() != tuple.getPayment_status()) {
											tuple.setPayment_status(newStatus.getStatus());
											tuple.setRefund_status(newStatus.getRefund_status());
										//System.out.print(tuple);
											return transactionRepo.save(tuple);
										//.thenReturn(Mono.just(""));
											}
										return Mono.empty();
										}
									)
				)
				.subscribe();
	}
	
}
