import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ModuleTest {

    val VALIDATION_JSON = "{\n" +
            "    \"cliContext\": {\n" +
            "        \"targetVer\": \"4.0.1\",\n" +
            "        \"sv\": \"4.0.1\"\n" +
            "    },\n" +
            "    \"filesToValidate\": [\n" +
            "        {\n" +
            "            \"fileName\": \"manually_entered_file.json\",\n" +
            "            \"fileContent\": \"{\\n\\t\\\"resourceType\\\":\\\"RiskAssessment\\\",\\n\\t\\\"status\\\":\\\"final\\\",\\n\\t\\\"subject\\\":{\\n\\t\\t\\\"reference\\\":\\\"Patient/1625\\\"\\n\\t},\\n\\t\\\"prediction\\\":[\\n\\t\\t{\\n\\t\\t\\t\\\"probabilityDecimal\\\":101.0\\n\\t\\t}\\n\\t]\\n}\",\n" +
            "            \"fileType\": \"json\"\n" +
            "        }\n" +
            "    ]\n" +
            "}"

    @Test
    fun testValidate() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "/validate") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
                setBody(VALIDATION_JSON)
            }.apply {
                println(VALIDATION_JSON)
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello, world!", response.content)
            }
        }


    }
}