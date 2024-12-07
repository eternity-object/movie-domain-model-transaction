package org.eternity.movie.persistence;

import jakarta.persistence.EntityManager;
import org.eternity.movie.reservation.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class JpaInheritanceTest {
	@Autowired
	private EntityManager em;

	@Test
	public void hierarchy_load() {
		DiscountPolicy policy =
				new PercentDiscountPolicy(0.1,
					Set.of(new SequenceCondition(1),
							new PeriodCondition(DayOfWeek.FRIDAY, LocalTime.of(9,0), LocalTime.of(12,0)),
							new SequenceCondition(3)));
		em.persist(policy);
		em.flush();
		em.clear();

		List<DiscountCondition> conditions =
				em.createQuery("select c from DiscountCondition c", DiscountCondition.class)
				  .getResultList();
		assertThat(conditions).hasSize(3);
	}

	@Test
	public void child_class_load() {
		DiscountPolicy policy =
				new PercentDiscountPolicy(0.1,
						Set.of(new SequenceCondition(1),
								new PeriodCondition(DayOfWeek.FRIDAY, LocalTime.of(9,0), LocalTime.of(12,0)),
								new SequenceCondition(3)));
		em.persist(policy);
		em.flush();
		em.clear();

		List<SequenceCondition> sequenceConditions =
				em.createQuery("select s from SequenceCondition s", SequenceCondition.class)
						.getResultList();
		assertThat(sequenceConditions).hasSize(2);

		List<PeriodCondition> periodConditions =
				em.createQuery("select p from PeriodCondition p", PeriodCondition.class)
						.getResultList();
		assertThat(periodConditions).hasSize(1);
	}
}
