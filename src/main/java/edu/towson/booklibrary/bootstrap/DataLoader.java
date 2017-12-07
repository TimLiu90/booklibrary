package edu.towson.booklibrary.bootstrap;


import edu.towson.booklibrary.domain.Book;
import edu.towson.booklibrary.domain.BookMeta;
import edu.towson.booklibrary.domain.Role;
import edu.towson.booklibrary.domain.User;
import edu.towson.booklibrary.service.BookService;
import edu.towson.booklibrary.service.RoleService;
import edu.towson.booklibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        Role role = new Role();
        role.setRole("ROLE_ADMIN");
        role.setDescription("Library Administrator");
        roleService.saveRole(role);

        /**
         * System administrator
         */
        User user = new User();
        user.setFirstName("Tim");
        user.setLastName("Liu");
        user.setUsername("admin");
        user.setEmail("admin@library.info");
        user.setTel("123456789");
        user.setActive(true);
        user.setPassword(bCryptPasswordEncoder.encode("abc"));
        user.setRole(role);
        userService.saveUser(user);

        role = new Role();
        role.setRole("ROLE_BORROWER");
        role.setDescription("Who borrow the books");
        roleService.saveRole(role);


    }
}
