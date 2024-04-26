package thecommerce.jh.user.service;

import org.springframework.stereotype.Service;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        List<User> foundUsers = userRepository.findByArguments(user);

        if(!foundUsers.isEmpty()) {
            checkDuplication(user, foundUsers);
        }

        return userRepository.insert(user);
    }

    @Override
    public List<User> retrieveUsers(int page, int pageSize, SortBy sortBy, boolean desc) {
        int offset = pageSize * (page - 1);
        return userRepository.findAll(offset, pageSize, sortBy, desc);
    }

    @Override
    public User updateUser(User user) {
        List<User> foundUsers = userRepository.findByArguments(user);
        if(foundUsers.size() != 1) {
            throw new CustomException(ErrorCode.NON_EXISTENT);
        }

        User updateUser = User.builder()
                .id(foundUsers.get(0).getId())
                .userId(foundUsers.get(0).getUserId())
                .password(foundUsers.get(0).getPassword())
                .name((user.getName() == null || user.getUserId().isEmpty()) ? foundUsers.get(0).getName() : user.getName())
                .nickname((user.getNickname() == null || user.getUserId().isEmpty()) ? foundUsers.get(0).getNickname() : user.getNickname())
                .phoneNumber((user.getPhoneNumber() == null || user.getUserId().isEmpty()) ? foundUsers.get(0).getPhoneNumber() : user.getPhoneNumber())
                .email((user.getEmail() == null || user.getUserId().isEmpty()) ? foundUsers.get(0).getEmail() : user.getEmail())
                .build();

        return userRepository.update(updateUser);
    }

    private static void checkDuplication(User user, List<User> compareList) {
        for (User compare : compareList) {
            if (Objects.equals(compare.getUserId(), user.getUserId())) {
                throw new CustomException(ErrorCode.DUPLICATED_USER_ID);
            }

            if (Objects.equals(compare.getPhoneNumber(), user.getPhoneNumber())) {
                throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
            }

            if (Objects.equals(compare.getEmail(), user.getEmail())) {
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
            }

            if (Objects.equals(compare.getNickname(), user.getNickname())) {
                throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
            }
        }
    }
}
