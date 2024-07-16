package model;

import model.user.User;
import model.user.UserDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    UserDAO userDAO;
    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }


    @DisplayName("생성 삭제 검색 통합테스트")
    @Test
    void userCDTest(){
        //given
        userDAO.insertUser("asdfasdfasdf","asdf","asdf","asdf");

        //when
        User user = userDAO.getUser("asdfasdfasdf");
        //userDAO.getUserList();

        //then
        userDAO.deleteUser("asdfasdfasdf");
        assertEquals("asdfasdfasdf",user.getUserId());
    }

    @DisplayName("없는 유저 검색 테스트")
    @Test
    void noUserTest(){
        //given

        //when
        User user = userDAO.getUser("aasdfasdfdf");
        //userDAO.getUserList();

        //then
        assertNull(user);
    }

    @AfterEach
    void tearDown() {
    }
}