package com.beTheDonor.repository;



import com.beTheDonor.entity.UserConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<UserConfirmationToken,Long>
{
            Optional<UserConfirmationToken> findByToken(String token);

        @Transactional
        @Modifying
        @Query("UPDATE UserConfirmationToken u " +
                "SET u.confirmedAt = ?2 " +
                "WHERE u.token = ?1")
        int updateConfirmedAt(String token,
                              LocalDateTime confirmedAt);


}
