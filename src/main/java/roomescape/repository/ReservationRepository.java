package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.Reservation;
import roomescape.domain.Status;
import roomescape.service.exception.ReservationNotFoundException;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean existsByThemeIdAndTimeIdAndDateAndStatus(long themeId, long timeId,
                                                     LocalDate date, Status status);

    boolean existsByMemberIdAndThemeIdAndTimeIdAndDateAndStatus(
            long memberId, long themeId, long timeId, LocalDate date, Status status);

    List<Reservation> findAllByThemeIdAndMemberIdAndDateBetween(long themeId, long memberId,
                                                                LocalDate from, LocalDate until);

    @EntityGraph(attributePaths = {"theme", "time"})
    List<Reservation> findAllByMemberId(long id);

    @EntityGraph(attributePaths = "theme")
    List<Reservation> findAllByStatusAndDateBetween(Status status, LocalDate from, LocalDate until);

    @EntityGraph(attributePaths = "time")
    List<Reservation> findAllByStatusAndDateAndThemeId(Status status, LocalDate date, long themeId);

    default Reservation findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
    }
}
