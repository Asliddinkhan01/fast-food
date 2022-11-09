package com.fastfood.entity.attachment;

import com.fastfood.template.EntityClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "attachments")
public class Attachment extends EntityClass {

    @Column
    private String originalName;

    @Column
    private float size;

    @Column
    private String contentType;

    @Column
    private String name;

}
