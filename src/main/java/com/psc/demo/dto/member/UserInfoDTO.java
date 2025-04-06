package com.psc.demo.dto.member;

import com.psc.demo.domain.member.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private String name;
    private String gender;
    private LocalDate birthDate;
    private String email;
    private String tel;

    // DTO -> Entity
    public UserInfo toEntity() {
        return UserInfo.builder()
                .name(this.name)
                .gender(this.gender)
                .birthDate(this.birthDate)
                .email(this.email)
                .tel(this.tel)
                .build();
    }

    // Entity -> DTO
    public static UserInfoDTO fromEntity(UserInfo entity) {
        return UserInfoDTO.builder()
                .name(entity.getName())
                .gender(entity.getGender())
                .birthDate(entity.getBirthDate())
                .email(entity.getEmail())
                .tel(entity.getTel())
                .build();
    }
}



