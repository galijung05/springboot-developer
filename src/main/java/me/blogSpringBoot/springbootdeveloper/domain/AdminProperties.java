package me.blogSpringBoot.springbootdeveloper.domain;

import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

// 관리자 사이트 설정 테이블 (PA_SITE_PROPERTIES)
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "adminPropertiesId"))
public class AdminProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 3317856778592897486L;

    public enum CheckType {
        CHECK,
        UNCHECK
    }

    @Id // 기본 키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값 자동 생성
    @Column(name = "adminPropertiesId")
    private Long adminPropertiesId;

    /** <code>sessionExpiredTime</code> : 세션 로그인 만료시간 */
    @Builder.Default
    @Column(nullable=false)
    @ApiModelProperty(value = "세션 로그인 만료시간 ", required = true)
    private int sessionExpiredTime = 120;

    /** <code>loginFailedCnt</code> : 로그인 실패 횟수 */
    @Builder.Default
    @Column(nullable=false)
    @ApiModelProperty(value = "로그인 실패 횟수", required = true)
    private int loginFailedCnt = 5;

    @Builder.Default
    @Column(nullable=false)
    private CheckType checkSessionExpired = CheckType.UNCHECK;

    @Column(updatable = false,columnDefinition = "DATETIME")
    private LocalDateTime createDate;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime updateDate;


    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updateDate = LocalDateTime.now();
    }

}
