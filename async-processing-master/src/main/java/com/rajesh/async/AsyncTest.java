package com.rajesh.async;

import com.rajesh.async.models.Employee;
import com.rajesh.async.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncTest {
    private static final String SYNC_PROCESSING_MODE = "sync";
    private static final String ASYNC_PROCESSING_MODE = "async";

    @Autowired
    private static EmployeeService employeeService;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        List<Employee> employeeList = getEmployees("async",employeeService);
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
/*
        long startTime2 = System.nanoTime();
        List<Employee> employeeList2 = getEmployees("async",employeeService);
        long stopTime2 = System.nanoTime();
        System.out.println(stopTime2 - startTime2);
*/
        //for (Employee employee:employeeList2) {
        //    System.out.println("employe name "+employee.getName()+"department "+employee.getDepartment());
       // }



}

    public static List<Employee> getEmployees(String mode, EmployeeService employeeService){
        List<Employee> employees = new ArrayList<Employee>();
        System.out.println("helooo");

        if (SYNC_PROCESSING_MODE.equals(mode)) {

            try {
                employees.addAll(employeeService.getTechEmployees());
                employees.addAll(employeeService.getBusinessEmployees());
            } catch (InterruptedException ex){
                System.out.println("Exception in calling employee service");
            }

        } else if (ASYNC_PROCESSING_MODE.equals(mode)) {

            try {
                Future<List<Employee>> techEmployeesFuture = employeeService.getTechEmployeesAsync();
                Future<List<Employee>> businessEmployeesFuture = employeeService.getBusinessEmployeesAsync();
                while (true) {
                    if (techEmployeesFuture.isDone() && businessEmployeesFuture.isDone() ) {
                        employees.addAll(techEmployeesFuture.get());
                        employees.addAll(businessEmployeesFuture.get());
                        break;
                    }
                }

            } catch (InterruptedException ex){
                System.out.println("Exception in calling employee service");
            } catch (ExecutionException ex){
                System.out.println("Exception getting the employee list from CompletableFuture");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "processing mode couldn't be found");
        }

        return employees;
    }
}