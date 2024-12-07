package org.eternity.movie.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.eternity.movie.generic.Money;
import org.eternity.movie.reservation.domain.Movie;
import org.eternity.movie.reservation.domain.PercentDiscountPolicy;
import org.eternity.movie.reservation.domain.SequenceCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
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

	@Test
	public void jpql_lock_mode() {
		em.persist(new Movie("영화1", 120, Money.wons(10000), null));
		em.persist(new Movie("영화2", 120, Money.wons(10000), null));
		em.flush();
		em.clear();

		List<Movie> movies = em.createQuery("select m from Movie m", Movie.class)
								.setLockMode(LockModeType.OPTIMISTIC)
							    .getResultList();

		long result = movies.stream().mapToLong(m -> m.getFee().longValue()).sum();

		assertThat(result).isEqualTo(20000);

		em.flush();
	}
}
