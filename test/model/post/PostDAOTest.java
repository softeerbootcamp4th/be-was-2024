package model.post;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostDAOTest {

    @Test
    void getNextPostIndex() {
        //given
        int postid = 100;
        //when

        PostDAO postDAO = new PostDAO();
        int nextid = postDAO.getNextPostIndex(postid);

        //then
        assertNotNull(nextid);
    }
}