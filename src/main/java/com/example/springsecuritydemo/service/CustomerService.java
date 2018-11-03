package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.entity.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class CustomerService implements UserDetailsService {

    static class Pair<K,V> {
        private K first;
        private V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }

    // initialize static map of users
    private static Map<String,Pair<String,String>> mapUsers = new HashMap<>();

    static {
        mapUsers.put("batman",new Pair<>("password","ROLE_USER"));
        mapUsers.put("ironman",new Pair<>("password","ROLE_ADMIN"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = loadCustomerByName(username);
        return new User(customer.getUsername(), customer.getPassword(),AuthorityUtils.createAuthorityList(mapUsers.get(username).second));
    }

    public Customer loadCustomerByName(String username) {
        return new Customer(username,mapUsers.get(username).first);
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
