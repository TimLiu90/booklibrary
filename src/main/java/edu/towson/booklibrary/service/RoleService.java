package edu.towson.booklibrary.service;


import edu.towson.booklibrary.domain.Role;

import java.util.List;

public interface RoleService {

    public Role findByRole(String role);
    public Role saveRole(Role role);
    public List<Role> getRoles();
}
