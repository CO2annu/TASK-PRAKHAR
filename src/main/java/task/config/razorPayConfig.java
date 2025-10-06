package task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class razorPayConfig {
	
	@Value("${razorpay.key}")
	private String key;
	
	@Value("${razorpay.value}")
	private String value;
	
    @Bean
    public WebClient razorPay(WebClient.Builder builder) {
        return builder.baseUrl("https://api.razorpay.com/v1")
                .defaultHeaders(headers -> headers.setBasicAuth(key, value))
                .build();
    }

}
