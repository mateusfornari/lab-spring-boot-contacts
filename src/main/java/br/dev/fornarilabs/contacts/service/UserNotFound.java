package br.dev.fornarilabs.contacts.service;

public class UserNotFound extends UserException {
    public UserNotFound(String message) {
        super(message);
    }
}
