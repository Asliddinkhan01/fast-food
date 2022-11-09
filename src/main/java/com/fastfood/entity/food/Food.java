package com.fastfood.entity.food;


import com.fastfood.entity.attachment.Attachment;
import com.fastfood.template.EntityClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "foods")
public class Food extends EntityClass {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @OneToOne
    private Attachment photo;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Category> categories;

}
