package com.douiou0.patientsmvc.sec.service;

import com.douiou0.patientsmvc.sec.entities.AppRole;
import com.douiou0.patientsmvc.sec.entities.AppUser;


public interface SecurityService {
    AppUser saveNewUser(String username, String password ,String rePassword);
    AppRole saveNewRole(String roleName ,String description);
    void addRoleToUser(String username, String roleName);
    void removeRoleFromUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    boolean userExists(String username);

}
