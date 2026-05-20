package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.service.InvalidCredentials;

public class ControllerUtils {

    public static User getAuthorizedUser(Object principal){
        if(principal instanceof User){
            return (User)principal;
        }
        throw new InvalidCredentials("The principal is not an User.");
    }
}
