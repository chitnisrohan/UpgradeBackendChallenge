package com.upgrade.virtualwallet.repository;

import com.upgrade.virtualwallet.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
