package ru.iate.gak.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.domain.Role;
import ru.iate.gak.domain.User;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.UserService;
import ru.iate.gak.service.impl.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class UserServiceImplTests {

    @TestConfiguration
    static class UserServiceImplTestsContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void whenGetRoles_thenReturnArrayOfRoles() {
        //given
        int rolesCount = Role.values().length;

        //when
        List<Role> found = userService.getAllRoles();

        //then
        assertEquals(found.size(), rolesCount);
    }

    @Test
    public void whenGetUsers_thenReturnArrayOfUsers() {
        //given
        String loginOne = "one";
        String loginTwo = "two";
        this.userService.saveUser(new User(loginOne));
        this.userService.saveUser(new User(loginTwo));

        //when
        List<User> found = this.userService.getAllUsers();

        //then
        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getLogin(), loginOne);
        assertEquals(found.get(1).getLogin(), loginTwo);
    }

    @Test
    public void whenGetUsersByRoles_thenReturnArrayOfUsersWhichContainsRole() {
        //given
        User user1 = new User("user1");
        user1.setRoles(new HashSet<>(Set.of(Role.PRESIDENT)));

        User user2 = new User("user2");
        user2.setRoles(new HashSet<>(Set.of(Role.PRESIDENT, Role.ADMIN)));

        String userLogin = "user3";
        User user3 = new User(userLogin);
        user3.setRoles(new HashSet<>(Set.of(Role.PRESIDENT, Role.MENTOR)));

        this.userService.saveUser(user1);
        this.userService.saveUser(user2);
        this.userService.saveUser(user3);

        //when
        List<User> found = this.userService.getAllUsersByRole(Role.MENTOR);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getLogin(), userLogin);
    }

    @Test
    public void whenSaveUser_thenReturnPlusOneUsers() {
        //given
        User user = new User();
        int countBeforeSave = this.userService.getAllUsers().size();

        //when
        this.userService.saveUser(user);
        int countAfterSave = this.userService.getAllUsers().size();

        //then
        assertEquals(countAfterSave, countBeforeSave + 1);
    }

    @Test
    public void whenUpdateUser_thenReturnChangedUser() {
        //given
        String login = "login";
        User user = new User(login);
        this.userService.saveUser(user);

        //when
        String newFirstname = "newFirstname";
        user.setFirstname(newFirstname);

        this.userService.updateUser(user);
        UserEntity foundAfterUpdate = userRepository.findByLogin(login);

        //then
        assertEquals(foundAfterUpdate.getFirstname(), newFirstname);
    }
}
