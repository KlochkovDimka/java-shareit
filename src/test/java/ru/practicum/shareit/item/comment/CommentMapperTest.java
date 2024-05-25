package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class CommentMapperTest {

    private final LocalDateTime createTime = LocalDateTime.now();
    private final Comment comment = new Comment(1L,
            "comment",
            new Item(),
            new User(1L, "user@email.com", "user"),
            createTime);

    private final CommentDto commentDto = new CommentDto(1L,
            "comment",
            "user",
            createTime);

    private final List<Comment> commentList = List.of(comment);


    @Test
    void convertToCommentDto() {
        CommentDto newCommentDto = CommentMapper.convertToCommentDto(comment);

        assertEquals(newCommentDto.getId(), comment.getId());
        assertEquals(newCommentDto.getText(), comment.getText());
        assertEquals(newCommentDto.getAuthorName(), comment.getAuthor().getName());
        assertEquals(newCommentDto.getCreated(), comment.getCreated());

    }

    @Test
    void convertToComment() {
        Comment newComment = CommentMapper.convertToComment(commentDto);

        assertEquals(commentDto.getText(), newComment.getText());
        assertEquals(commentDto.getCreated(), newComment.getCreated());
    }

    @Test
    void convertListToCommentDto() {
        List<CommentDto> commentDtoList = CommentMapper.convertListToCommentDto(commentList);

        assertEquals(commentDtoList.size(), commentList.size());
        assertEquals(commentDtoList.get(0).getId(), commentList.get(0).getId());
        assertEquals(commentDtoList.get(0).getText(), commentList.get(0).getText());
        assertEquals(commentDtoList.get(0).getAuthorName(), commentList.get(0).getAuthor().getName());
    }
}