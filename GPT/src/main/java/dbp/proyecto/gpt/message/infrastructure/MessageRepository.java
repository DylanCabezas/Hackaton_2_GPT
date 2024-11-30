package dbp.proyecto.gpt.message.infrastructure;

import dbp.proyecto.gpt.chat.domain.Chat;
import dbp.proyecto.gpt.message.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChat(Chat chat, Pageable pageable);

    Page<Message> findByChatId(Long chatId, Pageable pageable);

}
