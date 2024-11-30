package dbp.proyecto.gpt.chat.domain;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ChatMessage;
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
    private final MessageRepository messageRepository;
    private final AuthUtils authUtils;
    private final ModelMapper modelMapper;

    private final OpenAIClient openAIClient;

    private ChatResponseDto getChatResponseDto(Chat chat) {
        ChatResponseDto chatResponseDto = modelMapper.map(chat, ChatResponseDto.class);
        chatResponseDto.setUserId(chat.getUser().getId());
        List<MessageResponseDto> messageDtos = chat.getMessages().stream()
                .map(message -> modelMapper.map(message, MessageResponseDto.class))
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

    public ChatResponseDto processMessage(Long chatId, String userMessage) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new UserNotFoundException("Chat not found"));

        // Guardar el mensaje del usuario en la base de datos
        Message userMessageEntity = new Message();
        userMessageEntity.setChat(chat);
        userMessageEntity.setContent(userMessage);
        userMessageEntity.setCreatedAt(new java.util.LocalDateTime());
        messageRepository.save(userMessageEntity);

        // Generar respuesta con Azure OpenAI
        ChatCompletionOptions options = new ChatCompletionOptions()
                .setModel("gpt-4")
                .setMessages(List.of(
                        new ChatMessage("system", "You are an assistant."),
                        new ChatMessage("user", userMessage)
                ));
        String aiResponse = openAIClient.getChatCompletions(options).getChoices().get(0).getMessage().getContent();

        // Guardar la respuesta de Azure en la base de datos
        Message aiMessageEntity = new Message();
        aiMessageEntity.setChat(chat);
        aiMessageEntity.setContent(aiResponse);
        aiMessageEntity.setCreatedAt();
        messageRepository.save(aiMessageEntity);

        // Retornar el chat actualizado
        return getChatResponseDto(chat);
    }

}
