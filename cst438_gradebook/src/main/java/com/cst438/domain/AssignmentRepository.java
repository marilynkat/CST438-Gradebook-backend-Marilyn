package com.cst438.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends CrudRepository <Assignment, Integer> {

	@Query("select a from Assignment a where a.needsGrading=1 and a.dueDate < current_date and a.course.instructor= :email order by a.id")
	List<Assignment> findNeedGradingByEmail(@Param("email") String email);
	
	@Query("select a from Assignment a where a.name= :name and a.needsGrading=1")
   Assignment deleteAssignment(@Param("name") String name);
	
	@Modifying
	@Query("update Assignment a set a.name = :update where a.name = :name")
   void changeAssignName(@Param("name") String name,@Param("update") String update);
}
