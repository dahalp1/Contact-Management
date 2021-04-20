package com.mgmt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgmt.entities.ContactEntity;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
