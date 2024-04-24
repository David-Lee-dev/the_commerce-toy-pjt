package thecommerce.jh.user.service;

import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> retrieveUsers(int page, int pageSize, SortBy sortBy, boolean desc);

    User updateUser(User user);
}
