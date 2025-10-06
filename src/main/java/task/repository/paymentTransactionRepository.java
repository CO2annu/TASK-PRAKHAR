package task.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;
import task.model.Transaction;

public interface paymentTransactionRepository extends ReactiveCrudRepository<Transaction, String>{
	Mono<Transaction> findById(String id);
}
