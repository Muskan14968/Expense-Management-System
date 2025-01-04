package com.fullStack.expenseTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullStack.expenseTracker.models.GoalSetting;
import java.util.List;

@Repository
public interface GoalSettingRepository extends JpaRepository<GoalSetting, Long> {

    @Query(value = "SELECT g.* FROM goal_setting g JOIN users u ON g.user_id = u.id WHERE u.email = :email", nativeQuery = true)
    List<GoalSetting> getGoalsByUserId(String email);
}
