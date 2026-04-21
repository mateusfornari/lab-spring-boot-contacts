package br.dev.fornarilabs.contacts;

import org.springframework.boot.SpringApplication;

public class TestContactsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContactsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
