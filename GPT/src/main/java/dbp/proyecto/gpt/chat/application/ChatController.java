package dbp.proyecto.gpt.chat.application;

import dbp.proyecto.gpt.chat.domain.Chat;
import dbp.proyecto.gpt.chat.domain.ChatService;
import dbp.proyecto.gpt.chat.dto.ChatResponseDto;
import dbp.proyecto.gpt.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chats")
    public ResponseEntity<Page<ChatResponseDto>> getPostsByCurrentUser(Pageable pageable) {
        Page<ChatResponseDto> posts = chatService.getPostByCurrentUser(pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<ChatResponseDto> createChat(@RequestBody String chatName) {
        ChatResponseDto chatResponseDto = chatService.createChatForCurrentUser(chatName);
        return ResponseEntity.status(201).body(chatResponseDto);
    }

    @GetMapping("/chats/{chatId}")
    public ResponseEntity<ChatResponseDto> getChatById(@PathVariable Long chatId, Pageable pageable) {
        ChatResponseDto chatResponseDto = chatService.getChatWithMessages(chatId, pageable);
        return ResponseEntity.ok(chatResponseDto);
    }


}
