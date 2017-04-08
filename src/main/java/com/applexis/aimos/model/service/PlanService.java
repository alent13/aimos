package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Plan;

import java.util.List;

/**
 * Created by applexis on 08.04.2017.
 */
public interface PlanService {

    Plan create(Plan plan);

    void delete(Plan plan);
    void delete(Long id);

    Plan update(Plan plan);

    Plan findById(Long id);

    List<Plan> getPlans();

}
