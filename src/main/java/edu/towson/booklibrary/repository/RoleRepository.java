package edu.towson.booklibrary.repository;

import edu.towson.booklibrary.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Find role by role name
     * @param role
     * @return
     */
    public Role findByRole(String role);
}
