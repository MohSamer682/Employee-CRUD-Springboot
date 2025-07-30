package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dao.EmployeeDAO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

        private EmployeeDAO Employeedao;

        @Autowired
        public EmployeeServiceImpl(EmployeeDAO theEmployeeDAO) {
        Employeedao = theEmployeeDAO;
        }


    @Override
    public List<Employee> findAll() {
        return Employeedao.findAll();
    }

    @Override
    public Employee findbyId(int theId) {
        return Employeedao.findbyId(theId);
    }

    @Transactional
    @Override
    public Employee save(Employee theEmployee) {
        return Employeedao.save(theEmployee);
    }

    @Transactional
    @Override
    public void deletebyId(int theId) {
        Employeedao.deletebyId(theId);
    }
}
