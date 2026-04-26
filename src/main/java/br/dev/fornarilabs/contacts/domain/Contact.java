package br.dev.fornarilabs.contacts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_contact")
@Getter
@Setter
public class Contact extends Person{

    @Column(name = "phone_number")
    private String phoneNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
