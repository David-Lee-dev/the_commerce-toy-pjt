package thecommerce.jh.member.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @Nullable
    @Column(unique = true)
    private String nickname;

    @Nullable
    @Column(unique = true)
    private String phoneNumber;

    @Nullable
    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Member(String userId, String password, String name, String nickname, String phoneNumber, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
