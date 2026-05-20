package com.automarket.backend.repository;

import com.automarket.backend.model.AIChat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AIChatRepository extends JpaRepository<AIChat, UUID> {
    List<AIChat> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
