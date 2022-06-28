package com.beTheDonor;

import com.stripe.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BeTheDonorApplication {

	@PostConstruct
	public void setup()
	{
		Stripe.apiKey = "sk_test_51KktTMEJyazkyjbeOeMkWtzYD7Wt6p4LWzXoIewt5AyHS0gi5zTijqGkKmNB9PczogIYnRa4eybdEWrQrRdkoT2600vDhUvX8z";
	}

	public static void main(String[] args) {
		SpringApplication.run(BeTheDonorApplication.class, args);
	}

}
