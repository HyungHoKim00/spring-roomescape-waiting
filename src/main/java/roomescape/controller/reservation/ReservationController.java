package roomescape.controller.reservation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.controller.member.dto.LoginMember;
import roomescape.controller.reservation.dto.CreateReservationDto;
import roomescape.controller.reservation.dto.MyReservationResponse;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.controller.reservation.dto.ReservationSearchCondition;
import roomescape.controller.reservation.dto.UserCreateReservationRequest;
import roomescape.domain.Reservation;
import roomescape.domain.Status;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return reservationService.getReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @GetMapping("/mine")
    public List<MyReservationResponse> getLoginMemberReservation(final LoginMember member) {
        List<Reservation> reservations = reservationService.getReservationsByMember(member);
        return reservations.stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody @Valid final UserCreateReservationRequest userRequest,
            @Valid final LoginMember loginMember) {
        final CreateReservationDto reservationDto = new CreateReservationDto(
                loginMember.id(), userRequest.themeId(), userRequest.date(),
                userRequest.timeId(), Status.RESERVED);
        return createReservation(reservationDto);
    }

    @PostMapping("/waiting")
    public ResponseEntity<ReservationResponse> addReservationWaiting(
            @RequestBody @Valid final UserCreateReservationRequest userRequest,
            @Valid final LoginMember loginMember) {
        final CreateReservationDto reservationDto = new CreateReservationDto(
                loginMember.id(), userRequest.themeId(), userRequest.date(),
                userRequest.timeId(), Status.WAITING);
        return createReservation(reservationDto);
    }

    private ResponseEntity<ReservationResponse> createReservation(
            CreateReservationDto reservationDto) {
        final Reservation reservation = reservationService.addReservation(reservationDto);
        final URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(reservation.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(ReservationResponse.from(reservation));
    }

    @GetMapping(value = "/search", params = {"themeId", "memberId", "dateFrom", "dateTo"})
    public List<ReservationResponse> searchReservations(
            final ReservationSearchCondition request) {
        final List<Reservation> filter = reservationService.searchReservations(request);
        return filter.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") final Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent()
                .build();
    }
}
