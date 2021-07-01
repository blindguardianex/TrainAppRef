package script

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class FullTest extends Simulation{

  val httpConf = http
    .baseUrl("http://localhost:8083/api")
    .acceptHeader("application/json, */*")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val users = csv("users.csv").circular
  val tasks = csv("tasks.csv").random

  val auth =feed(users).exec(
              http("Auth")
                .post("/sign")
                .body(
                  StringBody("""{"username": "${user}", "password": "${password}"}""")).asJson
                .check(jsonPath("$..accessToken").optional.saveAs("accessToken"))
                .check(status.is(200))
    )

//  val postTask = repeat(3)(
//            feed(tasks)
//              .exec(
//                http("Post task")
//                  .post("/task/add")
//                  .header("Authorization","Bearer_${accessToken}")
//                  .body(
//                    StringBody("""{"num": "${taskBody}"}""")).asJson
//                  .check(status.is(200)))
//  )

  def randomDef() = Random.nextLong(Long.MaxValue)

  val postTask = repeat(20)(
      exec(
        http("Post task")
          .post("/tasks")
          .header("Authorization","Bearer ${accessToken}")
          .body(
            StringBody(session => s"""{"num": "${randomDef()}"}""")).asJson
          .check(status.is(200)))
  )

  val scn: ScenarioBuilder = scenario("Auth and post task")
    .exec(auth, doIf(session=>session.contains("accessToken")){
      exec(postTask)
    })

  setUp(
//    scn.inject(constantUsersPerSec(44) during(1 minutes))
    scn.inject(atOnceUsers(120))
//    scn.inject(rampUsers(2000) during(1 minutes))
  ).protocols(httpConf)
}
