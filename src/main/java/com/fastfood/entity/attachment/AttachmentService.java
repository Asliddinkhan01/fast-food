package com.fastfood.entity.attachment;

import com.fastfood.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.fastfood.util.AppConstants.PATH;


@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    private final UserRepository userRepository;


    public void deletePhoto(UUID fileId) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(fileId);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            attachmentRepository.deleteById(fileId);
            Path path = Paths.get(PATH + "/" + attachment.getName());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Attachment saveAttachment(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setSize(file.getSize());
        attachment.setContentType(file.getContentType());
        attachment.setOriginalName(file.getOriginalFilename());
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String name = UUID.randomUUID() + "." + split[split.length - 1];
        attachment.setName(name);
        Attachment saved = attachmentRepository.save(attachment);
        Path path = Paths.get(PATH + "/" + name);
        try {
            Files.copy(file.getInputStream(), path);
            return saved;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
