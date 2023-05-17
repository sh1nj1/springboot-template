package com.vrerv.springboottemplate.server.example.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.vrerv.springboottemplate.server.example.application.ExampleUserService
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserContactModel
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserCreateModel
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserResponseModel
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserUpdateModel
import com.vrerv.springboottemplate.server.example.domain.ExampleContact
import com.vrerv.springboottemplate.server.example.presentation.ExampleUserEndpoint
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import spock.lang.Shared
import spock.lang.Specification

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension.class)
class ExampleUserEndpointSpec extends Specification {

	@Shared
	MockMvc mockMvc

	ExampleUserService exampleUserService

	ObjectMapper objectMapper = new ObjectMapper()

	@BeforeEach
	void setup() {

		exampleUserService = Mock()
		println("exampleUserService: ${exampleUserService}")
		mockMvc = MockMvcBuilders.standaloneSetup(new ExampleUserEndpoint(exampleUserService))
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build()
	}

	def "list should return a paginated list of example users"() {

		given:
		def pageable = PageRequest.of(0, 20)
		def users = [ExampleUserResponseModel.builder()
							 .id(1L)
							 .name("John")
							 .contacts([ExampleUserContactModel.builder().contactType(ExampleContact.ExampleContactType.EMAIL).value("john@example.com").build()])
							 .build()]
		def page = new PageImpl<>(users, pageable, users.size())

		exampleUserService.list(_) >> page

		when:
		MvcResult result = mockMvc.perform(get("/example/example-users/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn()

		then:

		def response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class)
		response.content.size() == 1
		response.content[0].id == 1L
		response.content[0].name == "John"
	}

	def "create should create a new example user"() {

		given:
		def dto = ExampleUserCreateModel.builder()
				.name("John")
				.contacts([ExampleUserContactModel.builder().contactType(ExampleContact.ExampleContactType.EMAIL).value("john@example.com").build()])
				.build()
		def createdUser = ExampleUserResponseModel.builder()
				.id(1L)
				.name("John")
				.contacts([ExampleUserContactModel.builder().contactType(ExampleContact.ExampleContactType.EMAIL).value("john@example.com").build()])
				.build()

		exampleUserService.create(dto) >> createdUser

		when:
		MvcResult result = mockMvc.perform(post("/example/example-users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(dto)))
				.andExpect(status().isOk())
				.andReturn()

		then:
		def response = objectMapper.readValue(result.getResponse().getContentAsString(), ExampleUserResponseModel.class)
		response.id == 1L
		response.name == "John"
	}

	def "update should update an existing example user"() {

		given:
		def id = 1L
		ExampleUserUpdateModel dto = ExampleUserUpdateModel.builder()
				.name("John")
				.build()
		def updatedUser = ExampleUserResponseModel.builder()
				.id(id)
				.name("Jane")
				.contacts([ExampleUserContactModel.builder().contactType(ExampleContact.ExampleContactType.EMAIL).value("john@example.com").build()])
				.build()

		exampleUserService.update(id, dto) >> updatedUser

		when:
		MvcResult result = mockMvc
				.perform(put("/example/example-users/${id}")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(dto)))
				.andExpect(status().isOk())
				.andReturn()

		then:
		def response = objectMapper.readValue(result.getResponse().getContentAsString(), ExampleUserResponseModel.class)
		response.id == 1L
		response.name == "Jane"
	}

	def "delete should delete an existing example user"() {

		given:
		def id = 1L

		when:
		mockMvc.perform(delete("/example/example-users/${id}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())

		then:
		1 * exampleUserService.delete(id)
	}
}