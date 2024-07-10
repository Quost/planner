package io.github.mqdev.planner.trip;

import io.github.mqdev.planner.participant.ParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final ParticipantService participantService;

    private final TripRepository tripRepository;

    public TripController(ParticipantService participantService, TripRepository tripRepository) {
        this.participantService = participantService;
        this.tripRepository = tripRepository;
    }

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload tripRequest) {
        try {
            Trip trip = new Trip(tripRequest);
            var savedTrip = tripRepository.save(trip);
            participantService.registerParticipantsToTrip(tripRequest.emails_to_invite(), savedTrip.getId());
            return ResponseEntity.ok(new TripCreateResponse(savedTrip.getId(), savedTrip.getCreatedAt()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip updatedTrip = trip.get();
        updatedTrip.update(payload);

        return ResponseEntity.ok(tripRepository.save(updatedTrip));
    }
}