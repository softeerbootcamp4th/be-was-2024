package model;

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
        userDAO.insertUser("asdf","asdf","asdf","asdf");

        //when
        User user = userDAO.getUser("asdf");
        //userDAO.getUserList();

        //then
        userDAO.deleteUser("asdf");
        assertEquals("asdf",user.getName());
    }

    @AfterEach
    void tearDown() {
    }
}