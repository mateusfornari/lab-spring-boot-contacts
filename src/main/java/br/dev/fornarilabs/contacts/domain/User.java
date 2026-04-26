package br.dev.fornarilabs.contacts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
public class User extends Person {

    private String password;

}
