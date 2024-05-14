package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeRepositoryTest {

    @Autowired
    ReservationTimeRepository timeRepository;

    @Test
    @DisplayName("모든 예약 시간 목록을 조회한다.")
    void findAll() {
        assertThat(timeRepository.findAll()).isEmpty();
    }
}
