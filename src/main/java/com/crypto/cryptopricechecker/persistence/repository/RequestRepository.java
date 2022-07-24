package com.crypto.cryptopricechecker.persistence.repository;

import com.crypto.cryptopricechecker.persistence.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
}
