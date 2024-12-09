package org.eternity.movie.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.eternity.movie.generic.Money;
import org.eternity.movie.reservation.domain.AmountDiscountPolicy;
import org.eternity.movie.reservation.domain.Movie;
import org.eternity.movie.reservation.domain.PercentDiscountPolicy;
import org.eternity.movie.reservation.domain.SequenceCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class JpaTransactionTest {
	@Autowired
	private EntityManager em;

	@Autowired
	private TransactionTemplate txTemplate;

	@Test
	public void optimistic_lock_version() {
		Movie movie = new Movie("한산", 120, Money.wons(10000),
				new AmountDiscountPolicy("할인 정책", Money.wons(1000),
						Set.of(new SequenceCondition(1))));

		em.persist(movie);
		em.flush();
		em.clear();

		Movie loadedMovie = em.find(Movie.class, movie.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		loadedMovie.getDiscountPolicy().changeName("새로운 할인 정책");
		loadedMovie.changeFee(Money.wons(12000L));
		em.flush();
	}
}
