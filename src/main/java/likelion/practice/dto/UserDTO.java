package likelion.practice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String password;
    private String name;
    private String profileImage; // 프로필 이미지 (옵션)
}
