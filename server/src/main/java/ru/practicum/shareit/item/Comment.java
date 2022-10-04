package ru.practicum.shareit.item;


import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text_comment", nullable = false)
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Override
    public String toString() {
        return String.format("Comment = {id: %d, text: %s, created: %s}",
                id, text, created.format(CommentMapper.format));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;

        return this.getId().equals(comment.getId())
                && this.getText().equals(comment.getText())
                && this.getAuthor().equals(comment.getAuthor())
                && this.getItem().equals(comment.getItem())
                && this.getCreated().equals(comment.getCreated());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(id).hashCode() + Optional.ofNullable(text).hashCode()
                + Optional.ofNullable(author).hashCode()
                + Optional.ofNullable(item).hashCode() + Optional.ofNullable(created).hashCode() + 40;
    }
}
