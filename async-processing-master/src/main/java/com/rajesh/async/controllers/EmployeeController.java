package com.rajesh.async.controllers;

import com.rajesh.async.models.Employee;
import com.rajesh.async.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
public class EmployeeController {

    private static final String SYNC_PROCESSING_MODE = "sync";
    private static final String ASYNC_PROCESSING_MODE = "async";

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(method= RequestMethod.GET, value="/demo/employees")
    public List<Employee> getEmployees(@RequestParam String mode) { //
        List<Employee> employees = new ArrayList<>();

        if (SYNC_PROCESSING_MODE.equals(mode)) {

            try {
                employees.addAll(employeeService.getTechEmployees());
                employees.addAll(employeeService.getBusinessEmployees());
            } catch (InterruptedException ex) {
                System.out.println("Exception in calling employee service");
            }


        } else if (ASYNC_PROCESSING_MODE.equals(mode)) {

            try {

                Future<List<Employee>> techEmployeesFuture =
                        employeeService.getTechEmployeesAsync();
                Future<List<Employee>> businessEmployeesFuture =
                        employeeService.getBusinessEmployeesAsync();

                while (true) {
                    if (techEmployeesFuture.isDone() && businessEmployeesFuture.isDone()) {
                        employees.addAll(techEmployeesFuture.get());
                        employees.addAll(businessEmployeesFuture.get());
                        break;
                    }
                }

            } catch (InterruptedException ex) {
                System.out.println("Exception in calling employee service");
            } catch (ExecutionException ex) {
                System.out.println("Exception getting the employee " +
                        "list from CompletableFuture");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "processing mode couldn't be found");
        }

        return employees;
    }


}