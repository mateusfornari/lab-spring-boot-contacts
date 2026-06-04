package br.dev.fornarilabs.contacts.service;

public class ContactAlreadyExists extends ContactException {
    public ContactAlreadyExists(String message) {
        super(message);
    }
}
