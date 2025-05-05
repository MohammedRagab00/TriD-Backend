package com.gotrid.trid.security.userdetails;

import com.gotrid.trid.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + userEmail));
    }
}