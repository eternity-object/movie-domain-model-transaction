package org.eternity.movie.persistence;

import jakarta.persistence.EntityManager;
import org.eternity.movie.generic.Money;
import org.eternity.movie.reservation.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class JpaLazyLoadingTest {
	@Autowired
	private EntityManager em;

	@Test
	public void lazy_load() {
		Movie movie = new Movie(
				"한산",
				120,
				Money.wons(10000),
				new PercentDiscountPolicy(0.1,
						Set.of(new SequenceCondition(1))));

		em.persist(movie);
		em.flush();
		em.clear();

		Movie loadedMovie = em.find(Movie.class, movie.getId());
		assertThat(loadedMovie.getDiscountPolicy().getConditions()).isNotNull();
	}
}
