package com.team2.resumeeditorproject.admin.service;

import com.team2.resumeeditorproject.review.domain.Review;
import com.team2.resumeeditorproject.review.dto.ReviewDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewManagementService {
    String selectReview(Long reviewNo);
    Page<ReviewDTO> getPagedReviews(int pageNo, int size);
    Page<ReviewDTO> getDisplayReviews(int pageNo, int size);
    List<ReviewDTO> getVisibleReviews();
}
