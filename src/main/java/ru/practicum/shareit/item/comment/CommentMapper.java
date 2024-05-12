package ru.practicum.shareit.item.comment;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private CommentMapper() {
    }

    public static CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        commentDto.setAuthorName(comment.getAuthor().getName());
        return commentDto;
    }

    public static Comment convertToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public static List<CommentDto> convertListToCommentDto(List<Comment> comments) {
        if (comments.isEmpty()) {
            return List.of();
        }
        return comments.stream()
                .map(CommentMapper::convertToCommentDto)
                .collect(Collectors.toList());
    }
}
