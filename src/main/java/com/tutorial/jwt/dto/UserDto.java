package com.tutorial.jwt.dto;

import com.tutorial.jwt.entity.Authority;
import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.entity.UserAuthority;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDto {

   private String email;

   private String name;

   private Set<Authority> authorities = new HashSet<>();

   @Builder
   public UserDto(User user){
      email = user.getEmail();
      name = user.getName();
      authorities = user.getAuthorities().stream()
              .map(UserAuthority::getAuthority)
              .collect(Collectors.toSet());
   }

   public void updateAuthority(Authority authority){
      authorities.add(authority);
   }
}