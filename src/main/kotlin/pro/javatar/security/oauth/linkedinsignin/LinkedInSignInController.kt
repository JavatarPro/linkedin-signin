package pro.javatar.security.oauth.linkedinsignin

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
class LinkedInSignInController {

    var LINKENI_EMAIL_URI = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))"

    val LINEDKIN_DETAILS_URI = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))"

    @Value("\${linkedin.client-id}")
    val clientId: String? = null

    @Value("\${linkedin.client-secret}")
    val clientSecret: String? = null

    @Value("\${linkedin.redirect-uri}")
    val redirectUrl: String? = null

    @Value("\${linkedin.scope:r_liteprofile r_emailaddress}")
    val scope: String? = null

    var restTemplate = RestTemplate()
    var objectMapper = ObjectMapper()

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/home")
    fun home(model: Model): String {
        if (model.asMap().isEmpty()) {
            return "redirect:/"
        }
        return "home"
    }

    @GetMapping("/sign-in")
    fun authorization(): String {
        val authorizationUri = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=$clientId&redirect_uri=$redirectUrl&state=asasasasasas&scope=$scope"
        return "redirect:$authorizationUri"
    }

    @GetMapping("/exchange")
    fun exchange(@RequestParam("code") authorizationCode: String, model: RedirectAttributes): String {

        val accessToken = getAccessToken(authorizationCode)

        val detailsNode = getUserDetails(accessToken)
        val emailNode = getUserEmail(accessToken)

        model.addFlashAttribute("firstName", getUserName(detailsNode, "firstName"))
        model.addFlashAttribute("lastName", getUserName(detailsNode, "lastName"))
        model.addFlashAttribute("avatar", getAvatar(detailsNode))
        model.addFlashAttribute("email", getEmail(emailNode))

        return "redirect:home"
    }

    private fun getAccessToken(authorizationCode: String): String {
        val accessTokenUri = "https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&code=$authorizationCode&redirect_uri=$redirectUrl&client_id=$clientId&client_secret=$clientSecret"

        //store your access token
        val accessTokenResponse = restTemplate.getForObject(accessTokenUri, String::class.java)
        val accessTokenNode = objectMapper.readTree(accessTokenResponse)
        return accessTokenNode.get("access_token").asText()
    }

    private fun getUserEmail(accessToken: String): JsonNode? {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $accessToken")
        val entity = HttpEntity("parameters", headers)
        val emailResponse = restTemplate.exchange(LINKENI_EMAIL_URI, HttpMethod.GET, entity, String::class.java)
        return objectMapper.readTree(emailResponse.body)
    }

    private fun getUserDetails(accessToken: String): JsonNode? {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $accessToken")
        val entity = HttpEntity("parameters", headers)
        val linkedinDetailRequest = restTemplate.exchange(LINEDKIN_DETAILS_URI, HttpMethod.GET, entity, String::class.java)
        return objectMapper.readTree(linkedinDetailRequest.body)
    }

    private fun getUserName(node: JsonNode?, key: String): String? {
        return node!!.get(key).get("localized").get("en_US").asText();
    }

    private fun getAvatar(node: JsonNode?): String? {
        return node!!.get("profilePicture").get("displayImage~").get("elements").get(0).get("identifiers").get(0).get("identifier").asText()
    }

    private fun getEmail(node: JsonNode?): String {
        return node!!.get("elements").get(0).get("handle~").get("emailAddress").asText();
    }
}