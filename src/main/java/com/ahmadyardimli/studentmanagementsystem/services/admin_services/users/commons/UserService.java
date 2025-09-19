package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserStatus;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserType;
import com.ahmadyardimli.studentmanagementsystem.exceptions.NoChangesException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.common_mappers.UserDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;
import com.ahmadyardimli.studentmanagementsystem.security.JwtTokenProvider;
import com.ahmadyardimli.studentmanagementsystem.services.auth_services.AuthService;
import com.ahmadyardimli.studentmanagementsystem.services.auth_services.RefreshTokenService;
import com.ahmadyardimli.studentmanagementsystem.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends AuthService<User> {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserTypeService userTypeService;
    private final UserStatusService userStatusService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder,
                       UserTypeService userTypeService, UserStatusService userStatusService,
                       JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager,
                       RefreshTokenService refreshTokenService, RefreshTokenService refreshTokenService1) {
        super(jwtTokenProvider, authenticationManager, refreshTokenService);
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.userTypeService = userTypeService;
        this.userStatusService = userStatusService;
        this.refreshTokenService = refreshTokenService1;
    }

    public UserDTO registerUser(UserRequest userRequest) {
        if (userRequest.getUsername() == null || userRequest.getUsername().trim().isEmpty()) {
            throw new RequestValidationException("Username cannot be empty.");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().trim().isEmpty()) {
            throw new RequestValidationException("Password cannot be empty.");
        }

        checkIfUsernameAlreadyExists(userRequest.getUsername());

        // Extract user registration data from the UserRequest
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        String email = userRequest.getEmail();
        int userTypeId = userRequest.getUserTypeId();
        int userStatusId = userRequest.getStatusId();

        // Extract authKey and verificationToken from private methods
        String authKey = SecurityUtils.generateUniqueAuthKey();
        String verificationToken = SecurityUtils.generateUniqueVerificationToken();

        // Extract relational data
        UserType userType = userTypeService.getUserType(userTypeId);
        UserStatus userStatus = userStatusService.getStatus(userStatusId);

        // Set user to upcoming User Request values
        User user = new User();
        user.setUsername(username);
        user.setAuthKey(authKey); // Set the generated authKey
        user.setPasswordHash(passwordEncoder.encode(password));
        if(email.isEmpty()){
            user.setEmail(null);
        } else {
            user.setEmail(email);
        }
        // Set the generated verificationToken
        user.setVerificationToken(verificationToken);
        user.setUserType(userType);
        user.setUserStatus(userStatus);

        User savedUser = saveEntity(user);

        try {
            refreshTokenService.createRefreshToken(savedUser);
        } catch (Exception e) {
        }

        return userDTOMapper.apply(savedUser);
    }

    public UserDTO updateUser(int userId, UserRequest userRequest) {
        checkIfUserExistsOrThrow(userId);

        User existingUser = getUser(userId);

        boolean changes = false;


            if (userRequest.getEmail() != null){
                if ((userRequest.getEmail().isEmpty()) && existingUser.getEmail() != null){
                    existingUser.setEmail(null);
                    changes = true;
                }
                else if (!userRequest.getEmail().isEmpty() && !userRequest.getEmail().equals(existingUser.getEmail()) ) {
                    existingUser.setEmail(userRequest.getEmail());
                    changes = true;
                }
            }

            // User Type
            if (existingUser.getUserType() == null)
                existingUser.setUserType(new UserType());

            if (userRequest.getUserTypeId() > 0 && (userRequest.getUserTypeId() != existingUser.getUserType().getId())) {
                UserType userType = userTypeService.getUserType(userRequest.getUserTypeId());
                existingUser.setUserType(userType);
                changes = true;
            }

        int newStatusId = userRequest.getStatusId();
        if (newStatusId != -1 &&
                (existingUser.getUserStatus() == null || newStatusId != existingUser.getUserStatus().getId())) {
            UserStatus userStatus = userStatusService.getStatus(newStatusId);
            existingUser.setUserStatus(userStatus);
            changes = true;
        }


        if (userRequest.getUsername() != null && !userRequest.getUsername().equals(existingUser.getUsername())) {
                checkIfUsernameAlreadyExists(userRequest.getUsername());

                existingUser.setUsername(userRequest.getUsername());
                changes = true;
            }


            if (userRequest.getPassword() != null) {
                if (!passwordEncoder.matches(userRequest.getPassword(), existingUser.getPasswordHash())) {
                    String newPasswordHash = passwordEncoder.encode(userRequest.getPassword());
                    existingUser.setPasswordHash(newPasswordHash);
                    changes = true;
                }
            }

        if (!changes)
            throw new NoChangesException("No changes were made.");

        User updatedUser = userRepository.save(existingUser);
        return userDTOMapper.apply(updatedUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    // for controller, because it returns DTO
    public UserDTO getUserById(int userId) {
        checkIfUserExistsOrThrow(userId);
        return userRepository.findById(userId).map(userDTOMapper).orElse(null);
    }

    // for services to get User
    public User getUser(int userId) {
        checkIfUserExistsOrThrow(userId);
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUserById(int userId) {
        checkIfUserExistsOrThrow(userId);
        userRepository.deleteById(userId);
    }

    private void checkIfUserExistsOrThrow(int userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found.");
        }
    }

    public void checkIfUsernameAlreadyExists(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) throw new ResourceAlreadyExistsException("Username already exists.");
    }

    public User saveEntity(User user) {
        return userRepository.save(user);
    }


    public UserDTO getUserByUsernameFromDTO(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return userDTOMapper.apply(user);
        } else {
            throw new ResourceNotFoundException("User with username '" + username + "' not found.");
        }
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    public User findUserOrNull(int id) { return userRepository.findById(id).orElse(null); }
}
