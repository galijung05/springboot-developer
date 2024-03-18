package me.blogSpringBoot.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blogSpringBoot.springbootdeveloper.domain.AdminProperties;
import me.blogSpringBoot.springbootdeveloper.repository.PropertiesRepo;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PropertiesService {

    private final PropertiesRepo repo;
    @org.springframework.transaction.annotation.Transactional(readOnly=true)
    public AdminProperties get() throws Exception {
        return repo.findAll().stream().findFirst().get();
    }

    public int update(long id, AdminProperties updateProps) throws Exception {
        AdminProperties props = repo.findById(id).orElseThrow(() -> new IllegalArgumentException());
        AdminProperties setProps = props.toBuilder()
                .loginFailedCnt(updateProps.getLoginFailedCnt())
                .sessionExpiredTime(updateProps.getSessionExpiredTime())
                .checkSessionExpired(updateProps.getCheckSessionExpired())
                .build();
        setProps.setAdminPropertiesId(props.getAdminPropertiesId());
        setProps.setCreateDate(updateProps.getCreateDate());
        repo.save(setProps);
        return 200;
    }
}