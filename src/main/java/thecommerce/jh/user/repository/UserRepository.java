package thecommerce.jh.user.repository;

import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User insert(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUserId(String userId);

    List<User> findByArguments(User user);

    List<User> findAll(int offset, int limit, SortBy sortBy, boolean desc);

    User update(User user);
}
