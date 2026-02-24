package com.ecommerce.Ecom.repositories;

import com.ecommerce.Ecom.model.AppRole;
import com.ecommerce.Ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(AppRole appRole);
}
