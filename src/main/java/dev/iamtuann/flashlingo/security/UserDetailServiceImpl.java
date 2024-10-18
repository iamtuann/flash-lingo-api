package dev.iamtuann.flashlingo.security;

import com.sun.security.auth.UserPrincipal;
import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private AuthUserRepository authUserRepository;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        AuthUser user = authUserRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));
        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
