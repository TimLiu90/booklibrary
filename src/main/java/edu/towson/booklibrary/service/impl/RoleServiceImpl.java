package edu.towson.booklibrary.service.impl;

import edu.towson.booklibrary.domain.Role;
import edu.towson.booklibrary.repository.RoleRepository;
import edu.towson.booklibrary.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    /**
     * Find role by name
     * @param role
     * @return
     */
    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    /**
     * Save role
     * @param role
     * @return
     */
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    /**
     * Get role list
     * @return
     */
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
