package dbp.proyecto.gpt.chat.infrastructure;

import dbp.proyecto.gpt.chat.domain.Chat;
import dbp.proyecto.gpt.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findByUser(User user, Pageable pageable);

    Page<Chat> findByUserIdOrderByDateCreationDesc(Long userId, Pageable pageable);

}
