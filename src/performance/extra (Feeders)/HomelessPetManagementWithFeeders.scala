package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HomelessPetManagementWithFeeders extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

  val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"
  
  object Home {
    val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
  }
  
  object Login {
    val login = exec(
      http("Login")
			  .get("/login")
			  .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(30)
    .exec(
      http("Logged")
			  .post("/login")
			  .headers(headers_3)
			  .formParam("username", "vet1")
			  .formParam("password", "v3t1")
			  .formParam("_csrf", "${stoken}")
    ).pause(21)
  }
  
  object ListHomelessPets {
    val listHomelessPets = exec(http("ListHomelessPets")
			.get("/homeless-pets")
			.headers(headers_0))
		.pause(19)
  }
  
  object HomelessPetFormPositive {
    
    val feederPositive = csv("HomelessPetManagementPositive.csv")
    
    val homelessPetFormPositive = exec(http("HomelessPetFormPositive")
			.get("/homeless-pets/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(34)
    .feed(feederPositive)
    .exec(http("HomelessPetCreated")
			.post("/homeless-pets/new")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("name", "${name}")
			.formParam("birthDate", "${birthDate}")
			.formParam("type", "${type}")
			.formParam("_csrf", "${stoken}"))
		.pause(5)
  }
  
  object HomelessPetFormNegative {
    
    val feederNegative = csv("HomelessPetManagementNegative.csv")
    
    val homelessPetFormNegative = exec(http("HomelessPetFormNegative")
			.get("/homeless-pets/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(34)
    .feed(feederNegative)
    .exec(http("HomelessPetFormWithErrorMessage")
			.post("/homeless-pets/new")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("name", "${name}")
			.formParam("birthDate", "${birthDate}")
			.formParam("type", "${type}")
			.formParam("_csrf", "${stoken}"))
		.pause(28)
  }

  val homelessPetManagementPositiveScn = scenario("HomelessPetManagementPositive").exec(Home.home,
                                                                                        Login.login,
                                                                                        ListHomelessPets.listHomelessPets,
                                                                                        HomelessPetFormPositive.homelessPetFormPositive)
  
  val homelessPetManagementNegativeScn = scenario("HomelessPetManagementNegative").exec(Home.home,
                                                                                        Login.login,
                                                                                        ListHomelessPets.listHomelessPets,
                                                                                        HomelessPetFormNegative.homelessPetFormNegative)
		
	setUp(homelessPetManagementPositiveScn.inject(rampUsers(250) during (100 seconds)),
          homelessPetManagementNegativeScn.inject(rampUsers(250) during (100 seconds))
       ).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}