/**
 * Copyright 2015 jianglibo@gmail.com
 *
 */
package hello.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hello.domain.BootUser;
import hello.domain.Role;
import hello.repository.BootUserRepository;
import hello.repository.RoleRepository;
import hello.vo.BootUserVo;

/**
 * @author jianglibo@gmail.com
 *
 */
@Component
public class PersonUtil {
    
    private final BootUserRepository personRepo;
    private final RoleRepository roleRepo;
    
    @Autowired
    public PersonUtil(BootUserRepository personRepo, RoleRepository roleRepo) {
        this.personRepo = personRepo;
        this.roleRepo = roleRepo;
    }
    
    public void alterRoles(BootUser person, String...rns) {
        Set<Role> roles = Stream.of(rns).map(rn -> roleRepo.findByName(rn)).collect(Collectors.toSet());
        person.setRoles(roles);
        personRepo.save(person);
    }
    
    public void alterRoles(String username, String...rns) {
        BootUser person = personRepo.findByName(username);
        alterRoles(person, rns);
    }
    
    public BootUserVo createUnSavedPersonVo(String name, String...rns) {
        BootUser p = BootUser.newValidPerson();
        p.setName(name);
        p.setEmail(name + "@jianglibo.com");
        p.setPassword(name);
        p.setEmailVerified(true);
        p.setRoles(Stream.of(rns).map(rn -> new Role(rn)).collect(Collectors.toSet()));
        return new BootUserVo(p);
    }
}
