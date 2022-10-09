package com.splitwise.repositories;

import com.splitwise.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByMembers_Id(Long id);
}
