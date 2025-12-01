package com.batuhan.emg_service_product;

import com.batuhan.emg_service_product.controller.ProductController;
import com.batuhan.emg_service_product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {ProductController.class})
@AutoConfigureMockMvc(addFilters = false)
class EmgServiceProductApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

	@Test
	void contextLoads() {

	}
}