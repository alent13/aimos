package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Plan;
import com.applexis.aimos.model.repository.PlanRepository;
import com.applexis.aimos.model.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by applexis on 08.04.2017.
 */

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    public PlanRepository repository;

    @Override
    public Plan create(Plan plan) {
        return repository.save(plan);
    }

    @Override
    public void delete(Plan plan) {
        repository.delete(plan);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public Plan update(Plan plan) {
        return repository.save(plan);
    }

    @Override
    public Plan findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Plan> getPlans() {
        return repository.findAll();
    }
}
