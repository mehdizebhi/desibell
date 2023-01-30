package ir.desibell.notificationService.services.user;

import ir.desibell.notificationService.config.beanUpdate.BeanCopyConfig;
import ir.desibell.notificationService.entities.role.Role;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.repositories.user.UserRepository;
import ir.desibell.notificationService.services.resetPasswordToken.ResetPasswordTokenService;
import ir.desibell.notificationService.services.role.RoleService;
import ir.desibell.notificationService.services.security.SecurityService;
import ir.desibell.notificationService.services.subPanel.SubPanelService;
import ir.desibell.notificationService.services.subscribe.SubscribeService;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private SubPanelService subPanelService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    @Autowired
    private SecurityService securityService;

    public UserService(UserRepository userRepository, SubscribeService subscribeService, SubPanelService subPanelService, RoleService roleService, ResetPasswordTokenService resetPasswordTokenService, SecurityService securityService) {
        this.userRepository = userRepository;
        this.subscribeService = subscribeService;
        this.subPanelService = subPanelService;
        this.roleService = roleService;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.securityService = securityService;
    }

    @Transactional
    public User registerUser(User user) throws IllegalAccessException, InvocationTargetException {

        if (user.getPassword() != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        if (user.getId() != null && this.userRepository.getById(user.getId()) != null) {
            //update user fields on database
            User existUser = this.userRepository.getById(user.getId());
            if (isChangedNumber(user, existUser)) {
                this.updateAuthoritiesByNumber(existUser.getNumber(), user.getNumber());
            }
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existUser, user);
            return this.userRepository.save(existUser);
        }
        return this.userRepository.save(user);
    }

    //updated user without change password
    @Transactional
    public User updateExistUser(User user) throws IllegalAccessException, InvocationTargetException {
        if (user.getId() != null && this.userRepository.getById(user.getId()) != null) {
            User existUser = this.userRepository.getById(user.getId());
            if (isChangedNumber(user, existUser)) {
                this.updateAuthoritiesByNumber(existUser.getNumber(), user.getNumber());
            }
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existUser, user);
            return this.userRepository.save(existUser);
        }
        return null;
    }

    //updated user just subscribe
    @Transactional
    public User updateSubscribeExistUser(User user) throws IllegalAccessException, InvocationTargetException {
        if (user.getId() != null && this.userRepository.getById(user.getId()) != null) {
            user.setInvoices(null);
            user.setRoles(null);
            User existUser = this.userRepository.getById(user.getId());
            if (isChangedNumber(user, existUser)) {
                this.updateAuthoritiesByNumber(existUser.getNumber(), user.getNumber());
            }
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existUser, user);
            return this.userRepository.save(existUser);
        }
        return null;
    }

    @Transactional
    public User addSubscribeOfUser(Long id, Subscribe subscribe) {
        User user = this.userRepository.getById(id);
        if (user != null && subscribe != null) {
            user.addSubscribe(subscribe);
            return this.userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public User addRoleOfUser(Long id, Role role) {
        User user = this.userRepository.getById(id);
        if (user != null && role != null) {
            user.addRole(role);
            return this.userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public void addAuthority(Long id, String role) {
        User user = this.userRepository.getById(id);
        if (user != null) {
            List<Role> roles = this.findAllRolesOfUser(user);
            if (!roles.contains(this.roleService.findByName(role))) {
                this.userRepository.addAuthority(user.getNumber(), role);
            }
        }
    }

    @Transactional
    public User updatePasswordOfUser(Long id, String password) {
        User user = this.userRepository.getById(id);
        if (user != null && password != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            return this.userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public User updateCurrentSubscribe(User user) throws IllegalAccessException, InvocationTargetException {
        user = this.updateExistUser(user);
        user = this.updateSubscribes(user);
        if (!user.isActiveCurrentSubscribe()) {
            user = this.updateExistUser(user);
            user = this.deleteSubRoleByUserId(user.getId());
        }
        this.subPanelService.updateData(user.getSubPanel(), user);
        return user;
    }

    @Transactional
    public User updateSubscribes(User user) throws IllegalAccessException, InvocationTargetException {
        user = this.updateExistUser(user);
        List<Subscribe> subscribes = user.getSubscribes();
        for (Subscribe subscribe : subscribes) {
            if (!subscribe.isExpired() && (subscribe.isExpired() != subscribe.expirationCalculation())) {
                this.subscribeService.updateSubscribe(subscribe);
            }
        }
        return this.userRepository.save(user);
    }

    @Transactional
    public User deleteSubRoleByUserId(Long id) {
        User user = this.userRepository.getById(id);
        if (user != null) {
            this.userRepository.deleteSubAuthorities(user.getNumber());
            user.removeSubscribeRoles();
            return this.userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public void deleteAuthority(Long id, String role) {
        User user = this.userRepository.getById(id);
        if (user != null) {
            this.userRepository.deleteAuthority(user.getNumber(), role);
        }
    }

    @Transactional
    public User updateUserForMassNotification(User user) throws IllegalAccessException, InvocationTargetException {
        user = this.findById(user.getId());
        return this.updateCurrentSubscribe(user);
    }

    @Transactional
    public void updateAllCurrentSubscribesUsers() throws IllegalAccessException, InvocationTargetException {
        List<User> users = this.findAllUsers();
        for (User user : users) {
            this.updateCurrentSubscribe(user);
        }
    }

    @Transactional
    public void updateCurrentSubscribeAllUser() throws IllegalAccessException, InvocationTargetException {
        List<User> users = this.findAllUsers();
        for (User user : users) {
            this.updateCurrentSubscribe(user);
        }
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public User findById(Long id) {
        return this.userRepository.getById(id);
    }

    public User findBySubPanel(SubPanel subPanel) {
        User user = this.userRepository.findBySubPanel(subPanel.getId());
        return user;
    }

    public User findWithSubscribesById(Long id) {
        return this.userRepository.findUserWithSubscribesById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Transactional
    public void updateAuthoritiesByNumber(String existNumber, String number) {
        this.userRepository.updateAuthorities(existNumber, number);
    }

    public User findByNumber(String number) {
        return this.userRepository.findByNumber(number);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public long numberOfUsers() {
        return this.userRepository.numberOfUsers();
    }

    public long numberOfRoles(String role) {
        return this.userRepository.numberOfRoles(role);
    }

    public List<Role> findAllRolesOfUser(User user) {
        List<String> namesOfRols = this.userRepository.findAllNamesOfRolesByNumber(user.getNumber());
        List<Role> roles = new ArrayList<>();
        for (String n : namesOfRols) {
            roles.add(this.roleService.findByName(n));
        }
        return roles;
    }

    public Page<User> findAllUsersPage(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    public Page<User> allUserWithRoles(Pageable pageable) {
        Page<User> users = this.findAllUsersPage(pageable);
        for (User user : users) {
            user.setRoles(this.findAllRolesOfUser(user));
        }
        return users;
    }

    public boolean isChangedNumber(User user, User existUser) {
        if (user != null && existUser != null && !user.getNumber().equals(existUser.getNumber())) {
            return true;
        }
        return false;
    }

    public String whyUserCantEdit(User editUser) {
        String result = "";
        if (isEditNumber(editUser) && isExistNumber(editUser)) {
            result += result.isEmpty() ? "existNumber" : ",existNumber";
        }
        if (isEditEmail(editUser) && isExistEmail(editUser)) {
            result += result.isEmpty() ? "existEmail" : ",existEmail";
        }
        if (isEditPassword(editUser)) {
            if (!isMatchOldPassword(editUser)) {
                result += result.isEmpty() ? "notMatch" : ",notMatch";
            }
            if (!isSameConfirmPassword(editUser)) {
                result += result.isEmpty() ? "notSame" : ",notSame";
            }
            if (!isCorrectPassword(editUser.getPassword())) {
                result += result.isEmpty() ? whyIncorrectPassword(editUser.getPassword()) : "," + whyIncorrectPassword(editUser.getPassword());
            }
        }
        return result;
    }

    public String whyCantResetPassword(User user, String token) {
        String result = "";
        if (!isSameConfirmPassword(user)) {
            result += result.isEmpty() ? "notSame" : ",notSame";
        }
        if (!isCorrectPassword(user.getPassword())) {
            result += result.isEmpty() ? whyIncorrectPassword(user.getPassword()) : "," + whyIncorrectPassword(user.getPassword());
        }
        if (!this.securityService.isValidResetPasswordToken(token)) {
            result += result.isEmpty() ? "invalidToken" : ",invalidToken";
        }
        if (!this.resetPasswordTokenService.findByToken(token).getUser().getId().equals(user.getId())) {
            result += result.isEmpty() ? "invalidUser" : ",invalidUser";
        }
        return result;
    }

    public boolean doesCanResetPassword(User user, String token) {
        return isSameConfirmPassword(user) && isCorrectPassword(user.getPassword())
                && this.securityService.isValidResetPasswordToken(token)
                && this.resetPasswordTokenService.findByToken(token).getUser().getId().equals(user.getId());
    }

    public boolean doesCanEdit(User editUser) {
        if (isEditNumber(editUser) && isExistNumber(editUser)) {
            return false;
        } else if (isEditEmail(editUser) && isExistEmail(editUser)) {
            return false;
        } else if (isEditPassword(editUser)
                && (!isMatchOldPassword(editUser) || !isSameConfirmPassword(editUser)
                || !isCorrectPassword(editUser.getPassword()))) {
            return false;
        }
        return true;
    }

    public boolean isEditNumber(User editUser) {
        return !editUser.getNumber().equals(this.userRepository.getById(editUser.getId()).getNumber());
    }

    public boolean isEditEmail(User editUser) {
        return !editUser.getEmail().equals(this.userRepository.getById(editUser.getId()).getEmail());
    }

    public boolean isEditPassword(User editUser) {
        return !editUser.getOldPassword().isEmpty() || !editUser.getPassword().isEmpty() || !editUser.getConfirmPassword().isEmpty();
    }

    public boolean isMatchOldPassword(User editUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final String currentHashedPassword = this.userRepository.getById(editUser.getId()).getPassword();
        return bCryptPasswordEncoder.matches(editUser.getOldPassword(), currentHashedPassword);
    }

    public boolean isMatchPassword(String passowrd, String encodedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(passowrd, encodedPassword);
    }

    public boolean isSameConfirmPassword(User user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    public boolean doesCanRegister(User user) {
        return !isExistNumber(user) && !isExistEmail(user) && isSameConfirmPassword(user) && isCorrectPassword(user.getPassword());
    }

    public String whyUserCantRegistered(User user) {
        String result = "";
        if (isExistNumber(user)) {
            result += result.isEmpty() ? "existNumber" : ",existNumber";
        }
        if (isExistEmail(user)) {
            result += result.isEmpty() ? "existEmail" : ",existEmail";
        }
        if (!isSameConfirmPassword(user)) {
            result += result.isEmpty() ? "notSame" : ",notSame";
        }
        if (!isCorrectPassword(user.getPassword())) {
            result += result.isEmpty() ? whyIncorrectPassword(user.getPassword()) : "," + whyIncorrectPassword(user.getPassword());
        }

        return result;
    }

    public boolean isExistNumber(User user) {
        return this.findByNumber(user.getNumber()) != null;
    }

    public boolean isExistEmail(User user) {
        return this.findByEmail(user.getEmail()) != null;
    }

    public boolean isCorrectPassword(String password) {
        if (password.length() >= 6 && doesHaveLetter(password) && doesHaveNumber(password) && !doesHaveSpace(password)) {
            return true;
        }
        return false;
    }

    public String whyIncorrectPassword(String password) {
        String result = "";
        if (password.length() < 6) {
            result += result.isEmpty() ? "length" : ",length";
        }
        if (!doesHaveLetter(password)) {
            result += result.isEmpty() ? "letter" : ",letter";
        }
        if (!doesHaveNumber(password)) {
            result += result.isEmpty() ? "number" : ",number";
        }
        if (doesHaveSpace(password)) {
            result += result.isEmpty() ? "space" : ",space";
        }
        return result;
    }

    public boolean doesHaveLetter(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (((int) str.charAt(i) >= 65 && (int) str.charAt(i) <= 90)
                    || ((int) str.charAt(i) >= 97 && (int) str.charAt(i) <= 121)) {
                return true;
            }
        }
        return false;
    }

    public boolean doesHaveNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if ((int) str.charAt(i) >= 48 && (int) str.charAt(i) <= 57) {
                return true;
            }
        }
        return false;
    }

    public boolean doesHaveSpace(String str) {
        return str.contains(" ");
    }

}
