package thecommerce.jh.user.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class User {

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
    public User(Long id, String userId, String password, String name, String nickname, String phoneNumber, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
