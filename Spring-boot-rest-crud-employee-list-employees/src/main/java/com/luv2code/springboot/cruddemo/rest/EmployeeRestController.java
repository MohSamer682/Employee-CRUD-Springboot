package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.luv2code.springboot.cruddemo.dao.EmployeeDAO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.apache.tomcat.jni.SSLConf.apply;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;

    private ObjectMapper objectmapper;

    // quick and dirty: inject employee dao (use constructor injection)
    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeservice , ObjectMapper theobjectmapper ) {
        employeeService = theEmployeeservice;
        objectmapper = theobjectmapper;
    }

    // expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    // add mapping for GET /employees/{employeeId}

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {

        Employee  theEmployee = employeeService.findbyId(employeeId);

        if (theEmployee == null) {
          throw  new RuntimeException( "Employee id not found - " + employeeId);
        }
        return theEmployee;
    }


// add mapping for post /employees - add new employee

    @PostMapping("/employees")
    public Employee addEmployee (@RequestBody Employee theEmployee) {

        theEmployee.setId(0);

        Employee dbEmployee = employeeService.save(theEmployee);

        return theEmployee;
        }

        // add mapping for put /employees - update existing employee

        @PutMapping("/employees")
        public Employee updateEmployee(@RequestBody Employee theEmployee ) {
        Employee dbemployee = employeeService.save(theEmployee);
        return dbemployee;
        }

        // add mapping for PATCH /employees/{employeeid} - patch employee ... partial update

        @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId  , @RequestBody Map<String,Object> patchPayload)
        {
            Employee tempemployee = employeeService.findbyId(employeeId);

            if (tempemployee == null ) {
                throw new RuntimeException("Employee id not found - " + employeeId) ;
            }

            if (patchPayload.containsKey("id")) {
                throw new RuntimeException("Employee id not allowed in request body - " + employeeId);
            }

            Employee patchedEmployee = apply(patchPayload, tempemployee) ;

            Employee dbEmployee = employeeService.save(patchedEmployee);

            return dbEmployee;

        }

        private Employee apply(Map<String , Object> patchPayload , Employee tempEmployee) {

        // Convert employee object to a json object Node
        ObjectNode employeeNode = objectmapper.convertValue(tempEmployee, ObjectNode.class);

        // Convert patch payload map to json object node
        ObjectNode patchNode = objectmapper.convertValue(patchPayload,ObjectNode.class);

        // merge the patch updates into the employee node
        employeeNode.setAll(patchNode);

        return  objectmapper.convertValue(employeeNode,Employee.class);
    }

    // add mapping for DELETE /employees / {employeeId}

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findbyId(employeeId) ;

        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        employeeService.deletebyId(employeeId);

        return "Deleted employee Id - " + employeeId;
    }

}







