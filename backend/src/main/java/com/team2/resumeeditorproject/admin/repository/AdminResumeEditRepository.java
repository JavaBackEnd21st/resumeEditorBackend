package com.team2.resumeeditorproject.admin.repository;

import com.team2.resumeeditorproject.resume.domain.ResumeEdit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminResumeEditRepository extends JpaRepository<ResumeEdit, Long> {
    // 여기에 ResumeEdit 엔티티에 특화된 메서드를 추가할 수 있습니다.
    //ResumeEdit
    List<ResumeEdit> findByCompany(String company);
}