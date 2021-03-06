package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RehabManagementWithFeeders extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("lv-LV,lv;q=0.9,en-US;q=0.8,en;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")


  val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"
  

object Home {
val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(13)
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
			  .formParam("username", "trainer1")
			  .formParam("password", "tr41n3r")
			  .formParam("_csrf", "${stoken}")
    ).pause(23)




}

object FindOwnersPage {
val findownerspage = exec(http("FindOwnersPage")
			.get("/owners/find")
			.headers(headers_0))
		.pause(34)
}

object OwnerDetails {
val ownerdetails = exec(http("OwnerDetails")
			.get("/owners?lastName=Rodriquez")
			.headers(headers_0))
		.pause(22)
}

object NewRehabForm {
val newrehabform = exec(http("NewRehabForm")
			.get("/owners/3/pets/3/rehab/new")
			.headers(headers_0))
		.pause(31)
}
	
object AddedSuccessfulNewRehab {

val feederPositive = csv("RehabManagementPositive4.csv")

  val addedSuccessfulNewRehab2 = exec(http("AddedSuccessfulNewRehab")
			.get("/owners/3/pets/3/rehab/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(34) 
.feed(feederPositive)
.exec(http("AddedSuccessfulrehab")
			.post("/owners/3/pets/3/rehab/new")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "")
			.formParam("date", "${date}")
			.formParam("time", "${time}")
			.formParam("description", "${description}")
			.formParam("_csrf", "${stoken}"))
		.pause(37)


}



object UnsuccessfulAddingOfRehab {

val feederNegative = csv("RehabManagementNegative4.csv")

 val addedUnsuccessfulRehab = exec(http("UnsuccessfulAddingOfRehab")
			.get("/owners/3/pets/3/rehab/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(34) 
.feed(feederNegative)
.exec(http("UnsuccessfulAddingOfRehab2")
			.post("/owners/3/pets/3/rehab/new")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "")
			.formParam("date", "${date}")
			.formParam("time", "${time}")
			.formParam("description", "${description}")
			.formParam("_csrf", "${stoken}"))
		.pause(27)



}

val NewSuccessfulRehab = scenario("Successful").exec(
Home.home,
Login.login,
FindOwnersPage.findownerspage,
OwnerDetails.ownerdetails,
NewRehabForm.newrehabform,
AddedSuccessfulNewRehab.addedSuccessfulNewRehab2
)


val UnsuccessfulNewRehab = scenario("Unsuccessful").exec(
Home.home,
Login.login,
FindOwnersPage.findownerspage,
OwnerDetails.ownerdetails,
NewRehabForm.newrehabform,
UnsuccessfulAddingOfRehab.addedUnsuccessfulRehab 
)


setUp(NewSuccessfulRehab.inject(rampUsers(100) during (100 seconds)),
	      UnsuccessfulNewRehab.inject(rampUsers(100) during (100 seconds))

	).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )

}