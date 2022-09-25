package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepo;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:3001/", "http://localhost:3000/", "http://emp-mgmt-front.s3-website.ap-south-1.amazonaws.com/"})
public class EmployeeController {

	@Autowired
	private EmployeeRepo employeeRepo;

	// get all the employees
	@GetMapping("/employee")
	public List<Employee> getHello() {
		return employeeRepo.findAll();
	}

//	@GetMapping("/employee")
//	public String getHello() {
//		return "hello spring";
//	}

//	Get employee by Id
	@GetMapping("/employee/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
		Employee employee = employeeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		return ResponseEntity.ok(employee);
	}

	@PostMapping("/employee")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeRepo.save(employee);
	}

	@PutMapping("/employee/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
		Employee newEmp = employeeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		newEmp.setFirstname(employee.getFirstname());
		newEmp.setLastname(employee.getLastname());
		newEmp.setEmail(employee.getEmail());
		Employee finalEmp = employeeRepo.save(newEmp);
		return ResponseEntity.ok(finalEmp);
	}

	@DeleteMapping("/employee/{empids}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable String empids) {//long id) {
		
		String[]  ids = empids.split(",");
		List<Long> delemps = new ArrayList<Long>();
		
		for(int i=0;i<ids.length;i++)
				delemps.add(Long.parseLong(ids[i]));
		
//		List<Employee> delEmps = employeeRepo.findAllById(delemps);
//		Employee delEmp = employeeRepo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//		employeeRepo.delete(delEmp);
		employeeRepo.deleteAllById(delemps);
		Map<String, Boolean> response = new HashMap<String, Boolean>();

		response.put("Employees Deleted", true);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/employee/advSearch/{firstnm}&{lastnm}")
	public ResponseEntity<List<Employee>> advSearchEmployee(@PathVariable String firstnm, @PathVariable String lastnm){
		List<Employee> finalEmp = employeeRepo.findByFirstnameLikeOrLastnameLike("%"+firstnm+"%", "%"+lastnm+"%");
		return ResponseEntity.ok(finalEmp);
	}
}
