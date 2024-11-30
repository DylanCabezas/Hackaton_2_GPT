package dbp.proyecto.gpt.message.domain;

import dbp.proyecto.gpt.chat.domain.Chat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @NotNull
    @Enumerated(EnumType.STRING)
    private sender sender;

    @NotBlank(message = "Message mustn't be empty ")
    @Size(max = 500)
    private String content;

    @NotBlank
    private LocalDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private aiModel aiModel;


}
