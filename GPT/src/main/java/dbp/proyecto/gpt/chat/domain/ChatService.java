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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final AuthUtils authUtils;

    private final MessageRepository messageRepository;

    private ChatResponseDto getChatResponseDto(Chat chat) {
        // Primero, mapeamos los campos básicos usando ModelMapper
        ChatResponseDto chatResponseDto = modelMapper.map(chat, ChatResponseDto.class);

        // Establecemos el userId del propietario del chat
        chatResponseDto.setUserId(chat.getUser().getId());

        // Si existen mensajes en el chat, los mapeamos a MessageResponseDto
        List<MessageResponseDto> messageDtos = chat.getMessages().stream()
                .map(message -> {
                    // Mapeamos cada mensaje a su DTO
                    MessageResponseDto messageDto = modelMapper.map(message, MessageResponseDto.class);
                    return messageDto;
                })
                .collect(Collectors.toList());

        chatResponseDto.setMessages(messageDtos);

        return chatResponseDto;
    }

    public Page<ChatResponseDto> getPostByCurrentUser(Pageable pageable) {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        Page<Chat> chats = chatRepository.findByUserIdOrderByDateCreationDesc(user.getId(), pageable);
        return chats.map(this::getChatResponseDto);
    }

    public ChatResponseDto createChatForCurrentUser(String chatName) {
        String email =authUtils.getCurrentUserEmail();  // Usamos el email del usuario autenticado
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Chat chat = new Chat();
        chat.setUser(user);
        chat.setChatName(chatName);
        chat.setDateCreation(new java.util.Date());  // Asignar la fecha de creación

        // Guardar el chat
        Chat savedChat = chatRepository.save(chat);

        return getChatResponseDto(savedChat);
    }

    public ChatResponseDto getChatWithMessages(Long chatId, Pageable pageable) {
        // Obtener el chat por ID
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new UserNotFoundException("Chat not found"));

        // Obtener los mensajes paginados para este chat
        Page<Message> messages = messageRepository.findByChatId(chatId, pageable);

        // Mapear el chat a un DTO
        ChatResponseDto chatResponseDto = modelMapper.map(chat, ChatResponseDto.class);

        // Mapear los mensajes a MessageResponseDto
        List<MessageResponseDto> messageDtos = messages.stream()
                .map(message -> modelMapper.map(message, MessageResponseDto.class))
                .collect(Collectors.toList());

        // Establecer los mensajes paginados en el DTO del chat
        chatResponseDto.setMessages(messageDtos);

        return chatResponseDto;
    }

}
