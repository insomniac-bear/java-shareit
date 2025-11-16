package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    Collection<Item> findAllByUser_Id(Long userId);

    @Query("select it " +
            "from Item as it " +
            "where (lower(it.name) like lower(concat('%', ?1, '%')) " +
            "or lower(it.description) like lower(concat('%', ?1, '%')))" +
            "and it.available = true")
    Collection<Item> findAllByNameContainsOrDescriptionContains(String text);
}
