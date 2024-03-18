package me.blogSpringBoot.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blogSpringBoot.springbootdeveloper.domain.AdminUser;
import me.blogSpringBoot.springbootdeveloper.exception.EngineException;
import me.blogSpringBoot.springbootdeveloper.repository.AdminUserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * 유저 서비스 처리 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdminUserService implements UserDetailsService {

    private final AdminUserRepo repo;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + ", 유저가 존재하지 않습니다."));
    }

    public Optional<AdminUser> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public int create(AdminUser adminUser) throws Exception {
        repo.save(AdminUser.builder()
                .name(adminUser.getName())
                .status(AdminUser.Status.USE)
                .roleType(adminUser.getRoleType())
                .username(adminUser.getUsername())
                .password(encoder.encode(adminUser.getPasswordConfirm())).build());
        return 200;
    }

    public int update(long id, AdminUser paramUser) throws Exception {
        AdminUser adminUser =
                repo.findById(id).orElseThrow(() -> new EngineException(406));
        AdminUser setUser = AdminUser.builder()
                .status(paramUser.getStatus())
                .name(paramUser.getName())
                .username(adminUser.getUsername())
                .roleType(paramUser.getRoleType())
                .password(paramUser.getPasswordConfirm() == null || paramUser.getPasswordConfirm().isEmpty() ?
                        adminUser.getPassword() :
                        encoder.encode(paramUser.getPasswordConfirm())

                ).build();
        setUser.setAdminUserId(adminUser.getAdminUserId());
        repo.save(setUser);
        return 200;
    }

    @Transactional(readOnly=true)
    public AdminUser get(long id) throws Exception {
        AdminUser adminUser =
                repo.findById(id).orElseThrow(() -> new EngineException(406));
        return adminUser;
    }

    public int delete(long id) throws Exception {
        repo.deleteById(id);
        return 200;
    }
    @Transactional(readOnly=true)
    public int checkId(String username) throws Exception {
        Optional<AdminUser> existsUser = repo.findByUsername(username);
        return existsUser.isPresent()? 204 : 200;
    }
}
