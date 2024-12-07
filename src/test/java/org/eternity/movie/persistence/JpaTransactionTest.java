package org.eternity.movie.persistence;

import jakarta.persistence.EntityManager;
import org.eternity.movie.generic.Money;
import org.eternity.movie.reservation.domain.Movie;
import org.eternity.movie.reservation.domain.PercentDiscountPolicy;
import org.eternity.movie.reservation.domain.SequenceCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class JpaTransactionTest {
	@Autowired
	private EntityManager em;

	@Test
	public void optimistic_lock_version() {
		Movie movie = new Movie("한산", 120, Money.wons(10000), null);

		em.persist(movie);
		em.flush();
		em.clear();

		Movie loadedMovie = em.find(Movie.class, movie.getId());
		loadedMovie.changeFee(Money.wons(12000L));
		em.flush();
	}
}
