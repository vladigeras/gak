package ru.iate.gak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.TestsConfig;
import ru.iate.gak.dto.UserDto;
import ru.iate.gak.model.Role;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.service.impl.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        TestsConfig.class,
        UserServiceImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceImplTests {

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

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
        String login = "login";
        UserDto userDto = new UserDto();
        userDto.login = login;
        this.userService.saveUser(userDto);

        //when
        List<UserEntity> found = this.userService.getAllUsers();

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getLogin(), login);
    }

    @Test
    public void whenGetUsersByRoles_thenReturnArrayOfUsersWhichContainsRole() {
        //given
        UserDto user1 = new UserDto();
        user1.login = "user1";
        user1.roles = new HashSet<>(Set.of(Role.PRESIDENT));

        UserDto user2 = new UserDto();
        user2.login = "user2";
        user2.roles = new HashSet<>(Set.of(Role.PRESIDENT, Role.ADMIN));

        String userLogin = "user3";
        UserDto user3 = new UserDto();
        user3.login = userLogin;
        user3.roles = new HashSet<>(Set.of(Role.PRESIDENT, Role.MENTOR));

        this.userService.saveUser(user1);
        this.userService.saveUser(user2);
        this.userService.saveUser(user3);

        //when
        List<UserEntity> found = this.userService.getAllUsersByRole(Role.MENTOR);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getLogin(), userLogin);
    }

    @Test
    public void whenSaveUser_thenReturnPlusOneUsers() {
        //given
        UserDto user = new UserDto();
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
        UserDto user = new UserDto();
        user.login = login;
        this.userService.saveUser(user);

        //when
        String newFirstname = "newFirstname";
        user.firstname = newFirstname;

        this.userService.updateUser(user);
        List<UserEntity> found = userService.getAllUsers();

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getFirstname(), newFirstname);
    }
}
