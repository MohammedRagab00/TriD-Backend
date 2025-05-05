package com.gotrid.trid.marshmello.dto.Users;

import java.util.Optional;

public record Client(String token,
                     Optional<String> image) {
}
