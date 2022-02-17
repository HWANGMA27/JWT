package com.tutorial.jwt.service;

import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.entity.UserAuthority;
import com.tutorial.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
   private final UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      return userRepository.findByUserIdWithAuthority(email)
         .map(user -> createUser(email, user))
         .orElseThrow(()-> new RuntimeException("Not Found User"));
   }

   private org.springframework.security.core.userdetails.User createUser(String email, User user) {
      Set<UserAuthority> userAuthority = user.getAuthorities();
      List<GrantedAuthority> grantedAuthorities = userAuthority.stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().toString()))
              .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(user.getEmail(),
              user.getPassword(),
              grantedAuthorities);
   }
}
