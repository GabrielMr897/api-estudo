package br.com.apiEM.config;

import java.io.IOException;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import br.com.apiEM.utils.ReadJsonFileToJsonObject;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;

@OpenAPIDefinition
@Configuration
@SecurityScheme(name = "token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

	@Bean
	public OpenAPI baseOpenAPI() throws IOException {

		ReadJsonFileToJsonObject readJsonFileToJsonObject = new ReadJsonFileToJsonObject();

		ApiResponse badRequestAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read().get("badRequestResponse").toString()))))
				.description("Bad Request!");

		ApiResponse badCredentialsAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										readJsonFileToJsonObject.read().get("badCredentialsResponse").toString()))))
				.description("Bad Credentials!");

		ApiResponse forbiddenAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read().get("forbiddenResponse").toString()))))
				.description("Forbidden!");

		ApiResponse unprocessableEntityAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read().get("unprocessableEntityResponse").toString()))))
				.description("unprocessableEntity!");

		ApiResponse internalServerErrorAPI = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example()
										.value(readJsonFileToJsonObject.read().get("internalServerError").toString()))))
				.description("Internal Server Error!");

		Components components = new Components();
		components.addResponses("BadRequest", badRequestAPI);
		components.addResponses("badcredentials", badCredentialsAPI);
		components.addResponses("forbidden", forbiddenAPI);
		components.addResponses("unprocessableEntity", unprocessableEntityAPI);
		components.addResponses("internalServerError", internalServerErrorAPI);

		return new OpenAPI()
				.components(components)
				.info(new Info().title("Api Estudo")
						.version("V0.0.1")
						.description("API de loja de carros seminovos para estudo")
						.contact(new Contact().name("Gabriel Martins").email("gabriel-pms@hotmai.com")));
	}

	
	@Bean
	public GroupedOpenApi vehicleApi() {
		String[] paths = { "/api/vehicle/**" };
		return GroupedOpenApi.builder()
				.group("Vehicles")
				.pathsToMatch(paths)
				.build();
	}
	@Bean
	public GroupedOpenApi userApi() {
		String[] paths = { "/api/user/**" };
		return GroupedOpenApi.builder()
				.group("Users")
				.pathsToMatch(paths)
				.build();
	}
	@Bean
	public GroupedOpenApi authApi() {
		String[] paths = { "/api/authentication/**" };
		return GroupedOpenApi.builder()
				.group("Auth")
				.pathsToMatch(paths)
				.build();
	}
	@Bean
	public GroupedOpenApi categoriesApi() {
		String[] paths = { "/api/categories/**" };
		return GroupedOpenApi.builder()
				.group("Categories")
				.pathsToMatch(paths)
				.build();
	}
}
