package dbp.proyecto.gpt.message.domain;

import dbp.proyecto.gpt.chat.domain.Chat;
import dbp.proyecto.gpt.chat.infrastructure.ChatRepository;
import dbp.proyecto.gpt.message.infrastructure.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    public Page<Message> getMessagesByChat(Long chatId, Pageable pageable) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat no encontrado"));
        return messageRepository.findByChat(chat, pageable);
    }


}
