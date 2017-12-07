package edu.towson.booklibrary.repository;

import edu.towson.booklibrary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public User findByUsername(String username);
//    public List<User> findByParentUsername(String parentUserName);
//    public List<User> findByParentUsernameAndRoleRoleAndFirstNameAndLastNameIgnoreCase(String parentUserName, String role, String firstName, String lastName);
}
