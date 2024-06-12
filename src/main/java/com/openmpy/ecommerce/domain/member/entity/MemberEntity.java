package com.openmpy.ecommerce.domain.member.entity;

import com.openmpy.ecommerce.domain.member.entity.constants.RoleType;
import com.openmpy.ecommerce.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Builder
    public MemberEntity(String email, String password, RoleType role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}