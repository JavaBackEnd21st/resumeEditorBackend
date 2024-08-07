package com.team2.resumeeditorproject.resume.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.team2.resumeeditorproject.resume.domain.ResumeBoard;
import com.team2.resumeeditorproject.resume.dto.ResumeBoardDTO;
import com.team2.resumeeditorproject.resume.dto.request.ResumeBoardRequest;
import com.team2.resumeeditorproject.resume.service.ResumeBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ResumeBoardController.class)
public class ResumeBoardControllerTest {

    @Mock
    private ResumeBoardService resumeBoardService;

    @InjectMocks
    private ResumeBoardController resumeBoardController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(resumeBoardController).build();
    }

    @Test
    public void testSearchSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ResumeBoardDTO resumeBoard = ResumeBoardDTO.builder()
                .resumeBoardNo(1L)
                .rating(4.5f)
                .ratingCount(10)
                .readCount(100)
                .title("Test Title")
                .build();

        Page<ResumeBoardDTO> resultsPage = new PageImpl<>(Collections.singletonList(resumeBoard), pageable, 1);

        when(resumeBoardService.getPagedResumeBoardsContainingTitle(any(ResumeBoardRequest.class))).thenReturn(resultsPage);

        mockMvc.perform(get("/list/search")
                        .param("keyword", "test")
                        .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.response[0].resumeNo").value(1L))
                .andExpect(jsonPath("$.response[0].title").value("Test Title"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    public void testSearchNoResults() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ResumeBoardDTO> resultsPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(resumeBoardService.getPagedResumeBoardsContainingTitle(any(ResumeBoardRequest.class))).thenReturn(resultsPage);

        mockMvc.perform(get("/list/search")
                        .param("keyword", "empty")
                        .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.response").value("검색 결과가 없습니다."))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    public void testSearchInvalidPage() throws Exception {
        Pageable pageable = PageRequest.of(1, 5);
        ResumeBoardDTO resumeBoard = ResumeBoardDTO.builder()
                .resumeBoardNo(1L)
                .rating(4.5f)
                .ratingCount(10)
                .readCount(100)
                .title("Test Title")
                .build();

        Page<ResumeBoardDTO> resultsPage = new PageImpl<>(Collections.singletonList(resumeBoard), pageable, 1);

        when(resumeBoardService.getPagedResumeBoardsContainingTitle(any(ResumeBoardRequest.class))).thenReturn(resultsPage);

        mockMvc.perform(get("/list/search")
                        .param("keyword", "test")
                        .param("page", "2")) // Invalid page
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    public void testSearchServerError() throws Exception {
        when(resumeBoardService.getPagedResumeBoardsContainingTitle(any(ResumeBoardRequest.class))).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(get("/list/search")
                        .param("keyword", "error")
                        .param("page", "0"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Fail"))
                .andExpect(jsonPath("$.response").value("server error : search Fail Test Exception"));
    }
}
