package org.sualk.aiplayground

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class AiplaygroundApplicationTests {

	companion object {
		@Container
		val postgres = PostgreSQLContainer<Nothing>("postgres:18").apply {
			withDatabaseName("testdb")
			withUsername("test")
			withPassword("test")
		}

		@JvmStatic
		@DynamicPropertySource
		fun configureProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgres::getJdbcUrl)
			registry.add("spring.datasource.username", postgres::getUsername)
			registry.add("spring.datasource.password", postgres::getPassword)
		}
	}

	@Test
	fun contextLoads() {
	}

}
