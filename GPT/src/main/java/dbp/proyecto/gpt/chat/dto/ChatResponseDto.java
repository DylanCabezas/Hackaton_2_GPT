package dbp.proyecto.gpt.chat.dto;

import dbp.proyecto.gpt.message.domain.Message;
import dbp.proyecto.gpt.message.dto.MessageResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ChatResponseDto {
    private Long id;
    private String chatName;
    private Date dateCreation;
    private Long userId;  // ID del usuario que creó el chat
    private List<MessageResponseDto> messages;
}
