package com.example.demo.admincontrollers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.adminservices.AdminBusinessService;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/admin/business")
public class AdminBusinessController {

	AdminBusinessService adminBusinessService;

	public AdminBusinessController(AdminBusinessService adminBusinessService, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository, ProductRepository productRepository) {
		this.adminBusinessService = adminBusinessService;
	}
	
	@GetMapping("monthly")
	public ResponseEntity<?> getMonthlyBusiness(@RequestParam("month") String month1, @RequestParam("year") String year1) {
		try {
			System.out.println("input: " + month1 + " " + year1);
			int month = Integer.parseInt(month1);
			int year = Integer.parseInt(year1);
			System.out.println("year :" + year + " , month: " + month );
		Map<String, Object> response = adminBusinessService.calculateMonthlyBusiness(month, year);
		return ResponseEntity.status(HttpStatus.OK).body(response);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went Wrong");
		}

	}

	@GetMapping("/daily")
	public ResponseEntity<?> getDailyBusiness(@RequestParam String date) {

		try {
			System.out.println("date" + date);
			LocalDate localDate = LocalDate.parse(date);
			Map<String, Object> response = adminBusinessService.calculateDailyBusiness(localDate);
			return ResponseEntity.status(HttpStatus.OK).body(response);
				
			} catch (IllegalArgumentException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went Wrong");
			}
	}

	@GetMapping("/yearly")
	public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {

		try {
			System.out.println("year :" + year );
			Map<String, Object> response = adminBusinessService.calculateYearlyBusiness(year);
			return ResponseEntity.status(HttpStatus.OK).body(response);
				
			} catch (IllegalArgumentException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went Wrong");
			}
	}

	@GetMapping("overall")
	public ResponseEntity<?> getOverallBusiness() {

		try {
			Map<String, Object> response = adminBusinessService.calculateOverallBusiness();
			return ResponseEntity.status(HttpStatus.OK).body(response);
				
			} catch (IllegalArgumentException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went Wrong");
			}
	}

}
