package com.note.board.web;

import com.note.board.domain.Board;
import com.note.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // RANDOM_PORT 이어야 함

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("전체 조회 테스트")
    void readAll() {
        //given
        String url = "http://localhost:" + port + "/board/api/readAll";

        //when
        ResponseEntity<List<Board>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Board>>() {});

        //then
        assertThat(responseEntity.getStatusCode())
                .as("상태코드가 200이 아님")
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .as("결과 리스트가 비어있음")
                .isNotEmpty();

        Board firstSample = responseEntity.getBody().get(0); // 첫번째 하나만 꺼내서 확인
        assertThat(firstSample.getBoardIndex())
                .as("첫번쨰 테스트 샘플 ID가 1이 아님")
                .isEqualTo(1L);
    }

    @Test
    void read() {
        //given //테스트 DB에 id가 1인 데이터가 있음.
        int testSampleId = 1;
        String url = "http://localhost:" + port + "/board/api/read/" + testSampleId;

        //when
        ResponseEntity<Board> responseEntity =
                restTemplate.getForEntity(url, Board.class);

        //then
        assertThat(responseEntity.getStatusCode())
                .as("상태 코드가 200이 아님")
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getBoardIndex())
                .as("ID가 처음 조회한 ID가 아님")
                .isEqualTo(testSampleId);
    }
    
    @Test
    void read_Case_없는데이터조회() {
        //given
        int testSampleId = 4; // 테스트 DB에 id가 4인 데이터는 없음
        String url = "http://localhost:" + port + "/board/api/read/" + testSampleId;
        
        //when
        ResponseEntity<Board> responseEntity = 
                restTemplate.getForEntity(url, Board.class);
        
        //then
        assertThat(responseEntity.getStatusCode())
                .as("데이터가 없으면 상태 코드 204를 반환해야 함")
                .isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody())
                .as("body에는 아무것도 없어야 함")
                .isNull();
    }

    @Test
    void create() {
        //given
        String url = "http://localhost:" + port + "/board/api/create";
        String title = "TEST";
        Long writerId = 1L;

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("title", title);
        params.add("writer", writerId);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, null);

        //when
        ResponseEntity<Board> responseEntity =
                restTemplate.exchange(url, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<Board>() {});

        //then
        assertThat(responseEntity.getStatusCode())
                .as("상태 코드가 200 이어야 함")
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void create_When_제목이없으면_Then_400반환() {
        //given
        String url = "http://localhost:" + port + "/board/api/create";
        Long writerId = 1L;

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("title", title);
        params.add("writer", writerId);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, null);

        //when
        ResponseEntity<Board> responseEntity =
                restTemplate.exchange(url, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<Board>() {});

        //then
        assertThat(responseEntity.getStatusCode())
                .as("제목이 없으면 400 에러를 반환해야 함")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}