package me.blogSpringBoot.springbootdeveloper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PUBLIC)
@Builder(toBuilder = true)
@Getter
@AttributeOverride(name = "adminUserId", column = @Column(name = "adminUserId"))
public class AdminUser implements UserDetails {

    public enum RoleType {
        ROLE_USER,				// 유저 권한
        ROLE_ADMIN,				// 관리자 권한
        ROLE_SUPER_MANAGER		// 슈퍼 관리자 권한
    }

    public enum Status {
        USE, 						// 사용함
        NOT_USE,					// 사용안함
        FORCE_CHECKOUT,				// 강제 사용중지
        LOCKED						// 잠금상태
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminUserId")
    private Long adminUserId;

    @NotNull
    @Column(length=50, nullable=false)
    @ApiModelProperty(value = "관리자명", required = true)
    private String name;

    @Column( length=255, nullable=false)
    @ApiModelProperty(value = "관리자 비밀번호", required = true)
    private String password;

    @NotNull
    @Column(length=50, unique=true, nullable=false)
    @ApiModelProperty(value = "관리자 아이디", required = true)
    private String username;

    @Column(nullable=false)
    @ApiModelProperty(value = "관리자 권한", required = true)
    private RoleType roleType;

    @Builder.Default
    @Column(nullable=false)
    @ApiModelProperty(value = "관리자 상태", required = true)
    private Status status = Status.USE;

    @Transient
    @ApiModelProperty(value = "세션 유효 시간")
    private int sessionIntervalTime;

    @Transient
    private String nav;

    @Transient
    private String passwordConfirm;

    @Transient
    private AdminProperties.CheckType checkSessionExpired;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        RoleType[] roles = RoleType.values();
        int limitRoleType = roleType.ordinal();
        for (int i = 0, l = roles.length; i < l; ++i) {
            RoleType role = roles[i];
            if (i <= limitRoleType) {
                GrantedAuthority auth = new SimpleGrantedAuthority(role.name());
                list.add(auth);
            }
        }
        return list;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    public boolean hasRole(RoleType role) {
        if (getRoleType().ordinal() >= role.ordinal()) {
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setSessionIntervalTime(int sessionIntervalTime) {
        this.sessionIntervalTime = sessionIntervalTime;
    }


    public void setCheckSessionExpired(AdminProperties.CheckType checkSessionExpired) {
        this.checkSessionExpired = checkSessionExpired;
    }
}