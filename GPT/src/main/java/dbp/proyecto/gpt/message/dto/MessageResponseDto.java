package dbp.proyecto.gpt.message.dto;

import lombok.Data;
import dbp.proyecto.gpt.message.domain.sender;
import dbp.proyecto.gpt.message.domain.aiModel;
import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private Long id;
    private sender sender; // Puede ser el tipo de quien envía (usuario o IA)
    private String content;
    private LocalDateTime createdAt;
    private aiModel aiModel;
}
