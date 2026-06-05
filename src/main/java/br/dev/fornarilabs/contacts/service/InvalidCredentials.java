package br.dev.fornarilabs.contacts.service;

public class InvalidCredentials extends UserException {
    public InvalidCredentials(String message) {
        super(message);
    }
}
