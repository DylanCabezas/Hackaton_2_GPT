package dbp.proyecto.gpt.chat.domain;

import dbp.proyecto.gpt.Auth.utils.AuthUtils;
import dbp.proyecto.gpt.Exceptions.UserNotFoundException;
import dbp.proyecto.gpt.chat.dto.ChatResponseDto;
import dbp.proyecto.gpt.chat.infrastructure.ChatRepository;
import dbp.proyecto.gpt.message.domain.Message;
import dbp.proyecto.gpt.message.dto.MessageResponseDto;
import dbp.proyecto.gpt.message.infrastructure.MessageRepository;
import dbp.proyecto.gpt.user.domain.User;
import dbp.proyecto.gpt.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final AuthUtils authUtils;
    private final ModelMapper modelMapper;

    private ChatResponseDto getChatResponseDto(Chat chat) {
        ChatResponseDto chatResponseDto = modelMapper.map(chat, ChatResponseDto.class);
        chatResponseDto.setUserId(chat.getUser().getId());
        List<MessageResponseDto> messageDtos = chat.getMessages().stream()
                .map(message -> modelMapper.map(message, MessageResponseDto.class))
                .collect(Collectors.toList());
        chatResponseDto.setMessages(messageDtos);
        return chatResponseDto;
    }

    public Page<ChatResponseDto> getChatsByCurrentUser(Pageable pageable) {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Page<Chat> chats = chatRepository.findByUserIdOrderByDateCreationDesc(user.getId(), pageable);
        return chats.map(this::getChatResponseDto);
    }

    public ChatResponseDto createChatForCurrentUser(String chatName) {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Chat chat = new Chat();
        chat.setUser(user);
        chat.setChatName(chatName);
        chat.setDateCreation(new java.util.Date());

        Chat savedChat = chatRepository.save(chat);

        return getChatResponseDto(savedChat);
    }

    public ChatResponseDto getChatWithMessages(Long chatId, Pageable pageable) {
        // Find the chat
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new UserNotFoundException("Chat not found"));

        // Create the response DTO
        ChatResponseDto chatResponseDto = modelMapper.map(chat, ChatResponseDto.class);

        // Fetch paginated messages for this chat
        Page<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId, pageable);

        // Convert messages to DTOs
        List<MessageResponseDto> messageResponseDtos = messages.stream()
                .map(message -> modelMapper.map(message, MessageResponseDto.class))
                .collect(Collectors.toList());

        // Set messages in the chat response
        chatResponseDto.setMessages(messageResponseDtos);
        chatResponseDto.setTotalPages(messages.getTotalPages());
        chatResponseDto.setTotalMessages(messages.getTotalElements());

        return chatResponseDto;
    }

}