package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    List<Directory> findByParentIdAndUser(Long parentId, User user);

}
