package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.Role;
import ru.iate.gak.model.UserEntity;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenGetUsers_thenReturnAllUsers() {
        //given
        UserEntity userEntityOne = new UserEntity("loginOne");
        UserEntity userEntityTwo = new UserEntity("loginTwo");
        entityManager.persist(userEntityOne);
        entityManager.persist(userEntityTwo);
        entityManager.flush();

        //when
        List<UserEntity> founds = userRepository.findAll();

        //then
        assertThat(founds.size(), is(2));
        assertThat(founds.get(0).getLogin(), is(userEntityOne.getLogin()));
        assertThat(founds.get(1).getLogin(), is(userEntityTwo.getLogin()));
    }

    @Test
    public void whenGetUserByLogin_thenReturnUser() {
        //given
        String login = "login";
        UserEntity userEntity = new UserEntity(login);
        entityManager.persist(userEntity);
        entityManager.flush();

        //when
        UserEntity found = userRepository.findByLogin(login);

        //then
        assertEquals(found.getLogin(), userEntity.getLogin());
    }

    @Test
    public void whenGetUserByRoles_thenReturnUsersWhichContainsRoles() {
        //given
        UserEntity userEntityOne = new UserEntity();
        userEntityOne.setRoles(Set.of(Role.PRESIDENT, Role.ADMIN));
        UserEntity userEntityTwo = new UserEntity();
        userEntityTwo.setRoles(Set.of(Role.ADMIN, Role.REVIEWER));
        entityManager.persist(userEntityOne);
        entityManager.persist(userEntityTwo);
        entityManager.flush();

        //when
        List<UserEntity> found = userRepository.findAllByRoles(Set.of(Role.PRESIDENT));

        //then
        assertEquals(found.size(), 1);
        assertTrue(found.get(0).getRoles().contains(Role.PRESIDENT));
    }
}
