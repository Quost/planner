package io.github.mqdev.planner.trip;

import java.util.List;

public record TripCreatePayload(
        String destination,
        String starts_at,
        String ends_at,
        String owner_name,
        String owner_email,
        List<String> emails_to_invite
) {}