package com.gotrid.trid.auth.RefreshToken;

import com.gotrid.trid.user.Users;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RefreshTokenSpecification {

    public static Specification<RefreshToken> tokenBelongsToUser(Integer userId) {
        return (root, query, cb) -> {
            Join<RefreshToken, Users> userJoin = root.join("user");
            return cb.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<RefreshToken> isNotRevoked() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("revoked"), false);
    }

    public static Specification<RefreshToken> findActiveTokensByUser(Integer userId) {
        return Specification
                .where(tokenBelongsToUser(userId))
                .and(isNotRevoked());
    }

}
