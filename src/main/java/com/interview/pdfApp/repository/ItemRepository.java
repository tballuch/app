package com.interview.pdfApp.repository;

import com.interview.pdfApp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
