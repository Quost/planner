package io.github.mqdev.planner.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Trip(TripRequestPayload payload) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        this.destination = payload.destination();
        this.isConfirmed = false;
        this.startsAt = LocalDateTime.parse(payload.starts_at(), formatter);
        this.endsAt = LocalDateTime.parse(payload.ends_at(), formatter);
        this.ownerName = payload.owner_name();
        this.ownerEmail = payload.owner_email();
        this.createdAt = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
    }
}