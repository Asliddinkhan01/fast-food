package com.fastfood.entity.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    @Query(nativeQuery = true,
            value = "select a.name\n" +
                    "from foods f\n" +
                    "         join attachments a on f.photo_id = a.id\n" +
                    "where f.id = :foodId")
    String getNameById(UUID foodId);
}
