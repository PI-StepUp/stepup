package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.dao.comment.CommentRepository;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class CommentRespositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    private User writer;
    private Talk talk;
    private Comment comment1;
    private Comment comment2;

    @Test
    @BeforeEach
    public void init() {
        writer = User.builder()
                .id("j3beom")
                .build();

        em.persist(writer);
        em.flush();

        talk = Talk.builder()
                .writer(writer)
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();

        em.persist(talk);
        em.flush();

        comment1 = Comment.builder()
                .writer(writer)
                .content("댓글이지롱")
                .board(talk)
                .build();

        comment2 = Comment.builder()
                .writer(writer)
                .content("댓글인데여")
                .board(talk)
                .build();
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    public void testInsert() {
        // When
        Comment savedComment1 = commentRepository.insert(comment1);
        Comment savedComment2 = commentRepository.insert(comment2);
        // Then
        assertThat(savedComment1.getCommentId()).isNotNull();
        assertThat(savedComment2.getCommentId()).isNotNull();
    }

    @Test
    @DisplayName("댓글 상세 조회 테스트")
    public void testFindOne() {
        // Given
        testInsert();

        // When
        Optional<Comment> foundComment = commentRepository.findOne(comment1.getCommentId());

        // Then
        assertTrue(foundComment.isPresent());
        assertThat(foundComment.get().getCommentId()).isEqualTo(comment1.getCommentId());
    }

    @Test
    @DisplayName("해당 게시글에 대한 댓글 전체 조회 테스트")
    public void testfindByBoardId() {
        // Given
        testInsert();

        // When
        List<Comment> comments = commentRepository.findByBoardId(comment1.getBoard().getBoardId());
        int numberOfMeetingsFound = comments.size();

        // Then
        assertThat(2).isEqualTo(numberOfMeetingsFound);
    }

    @Test
    @DisplayName("정모 게시글 삭제 테스트")
    public void testDelete() {
        // Given
        testInsert();

        // When
        commentRepository.delete(comment1.getCommentId());

        // Then
        Comment deletedComment = em.find(Comment.class, comment1.getCommentId());
        assertNull(deletedComment);
    }
}