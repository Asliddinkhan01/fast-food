package com.fastfood.entity.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {

    @Query(nativeQuery = true,
            value = "select categories\n" +
                    "from foods_categories\n" +
                    "where foods_id = :foodId")
    List<String> getCategoriesById(UUID foodId);


    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as id,\n" +
                    "       s.name                as name,\n" +
                    "       s.description         as description,\n" +
                    "       s.price               as price\n" +
                    "from foods s\n" +
                    "where id = :foodId\n" +
                    "and is_deleted = false")
    Optional<FoodProjection> getFoodById(UUID foodId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as id,\n" +
                    "       s.name                as name,\n" +
                    "       s.description         as description,\n" +
                    "       s.price               as price\n" +
                    "from foods s\n" +
                    "where lower(s.name) like lower(concat('%', :search, '%'))\n" +
                    "and is_deleted = false")
    List<FoodProjection> getAllFoods(String search);


    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as id,\n" +
                    "       s.name                as name,\n" +
                    "       s.description         as description,\n" +
                    "       s.price               as price\n" +
                    "from foods s\n" +
                    "         join foods_categories fc on s.id = fc.foods_id\n" +
                    "where fc.categories = :category\n" +
                    "and is_deleted = false")
    List<FoodProjection> getFoodsByCategory(String category);
}
