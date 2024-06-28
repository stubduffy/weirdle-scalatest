import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxOptions}
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatestplus.selenium.*
import org.scalatest.*
import org.scalatest.matchers.*


class WeirdleTest extends flatspec.AnyFlatSpec with should.Matchers with WebBrowser with BeforeAndAfterAll {

  // need this if using chrome:
  // System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver")
  val options: FirefoxOptions = new FirefoxOptions().addArguments("--headless")
  implicit val driver: WebDriver = new FirefoxDriver(options)

  override def afterAll(): Unit = {
    driver.quit()
    super.afterAll()
  }

  val url  = "https://stubduffy.github.io"
  val losingGuesses: Seq[String] = Seq("bread", "horse", "ghost", "crate", "money", "tooth")

  def makeGuess(g: String): Unit = {
    textField("guess").value = g
    click on id("submitButton")
  }

  def checkAfterGuess(guessNumber: Int, guess: String): Unit = {
    val guessesRemaining = losingGuesses.size - guessNumber
    find(id("guesses")).get.text should equal (guessesRemaining.toString)

    val colors = for j <- 0 to 4 yield {
      val cell = getCell(guessNumber, j)
      cell.getText should equal(guess(j).toString)
      cell.getAttribute("style")
    }

    if (guessesRemaining > 0 && colors.forall(_.contains("background-color: black"))) {
      pageSource should include ("Blackout, eek, must try harder!")
    }

  }

  def getCell(guessNumber: Int, column: Int): WebElement = {
    val rows = findAll(tagName("tr")).toIndexedSeq
    val row = rows(guessNumber - 1)
    row.underlying.findElements(By.tagName("td")).get(column + 1) // we increment column since the first always contains 'Guess:'
  }

  "Weirdle" should "be hard" in {
    go to url

    for i <- 0 to 5 do {
      makeGuess(losingGuesses(i))
      checkAfterGuess(i + 1, losingGuesses(i))
    }

    pageSource should include ("Sorry, you're too rubbish")
  }

  "Weirdle" should "alert when invalid word entered" in {
    go to url

    def acceptExpectedAlert(): Unit = {
      val alert = switch to alertBox
      alert.getText should include ("Word does not exist, try again.")
      alert.accept()
    }

    makeGuess("elephant")

    acceptExpectedAlert()

    makeGuess("dgpql")

    acceptExpectedAlert()

    makeGuess("abc")

    acceptExpectedAlert()

  }
}
