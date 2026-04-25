package br.dev.fornarilabs.contacts.service;

public class UserAlreadyExists extends UserException {
    public UserAlreadyExists(String message) {
        super(message);
    }
}
