package thecommerce.jh.user.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        try {
            return userRepository.insert(user);
        } catch (Exception e) {
            if(e instanceof DataIntegrityViolationException) {
                throw new CustomException(ErrorCode.DUPLICATED_USER_DATA, e);
            } else {
                throw new CustomException(ErrorCode.UNKNOWN, e);
            }
        }
    }

    @Override
    public List<User> retrieveUsers(int page, int pageSize, SortBy sortBy, boolean desc) {
        int offset = pageSize * (page - 1);
        return userRepository.findAll(offset, pageSize, sortBy, desc);
    }

    @Override
    public User updateUser(User userDataForUpdate) {
        Optional<User> foundUsers = userRepository.findByUserId(userDataForUpdate.getUserId());

        if(!foundUsers.isPresent()) {
            throw new CustomException(ErrorCode.NON_EXISTENT);
        }

        User updateUser = User.builder()
                .id(foundUsers.get().getId())
                .userId(foundUsers.get().getUserId())
                .password(foundUsers.get().getPassword())
                .name((userDataForUpdate.getName() == null || userDataForUpdate.getUserId().isEmpty()) ? foundUsers.get().getName() : userDataForUpdate.getName())
                .nickname((userDataForUpdate.getNickname() == null || userDataForUpdate.getUserId().isEmpty()) ? foundUsers.get().getNickname() : userDataForUpdate.getNickname())
                .phoneNumber((userDataForUpdate.getPhoneNumber() == null || userDataForUpdate.getUserId().isEmpty()) ? foundUsers.get().getPhoneNumber() : userDataForUpdate.getPhoneNumber())
                .email((userDataForUpdate.getEmail() == null || userDataForUpdate.getUserId().isEmpty()) ? foundUsers.get().getEmail() : userDataForUpdate.getEmail())
                .build();

        try {
            return userRepository.update(updateUser);
        } catch (Exception e) {
            if(e instanceof DataIntegrityViolationException) {
                throw new CustomException(ErrorCode.DUPLICATED_USER_DATA, e);
            } else {
                throw new CustomException(ErrorCode.UNKNOWN, e);
            }
        }
    }
}
