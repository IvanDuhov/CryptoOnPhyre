package com.crypto.cryptopricechecker.persistence.repository;

import com.crypto.cryptopricechecker.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByAuthToken(String authToken);

}
