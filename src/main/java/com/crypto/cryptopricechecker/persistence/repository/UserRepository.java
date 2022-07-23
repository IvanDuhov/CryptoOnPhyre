package com.crypto.cryptopricechecker.persistence.repository;

import com.crypto.cryptopricechecker.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
