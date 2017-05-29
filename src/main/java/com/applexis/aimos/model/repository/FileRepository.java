package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.File;
import com.applexis.aimos.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByParentDirectoryAndUser(Directory parentDirectory, User user);
    File findFirstByPathAndNameAndUser(String path, String name, User user);

}
