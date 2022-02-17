package com.tutorial.jwt.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
@Entity
public class UserAuthority {

    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private Long authorityId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void updateUserAuthority (){
        this.user.getAuthorities().add(this);
    }
}
