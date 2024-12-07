package org.eternity.movie.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eternity.movie.generic.Money;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screening {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sequence;
    private LocalDateTime screeningTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="MOVIE_ID")
    private Movie movie;

    public Screening(int sequence, LocalDateTime screeningTime) {
        this.sequence = sequence;
        this.screeningTime = screeningTime;
    }

    public Money getFixedFee() {
        return movie.getFee();
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public LocalDateTime getStartTime() {
        return screeningTime;
    }
}
