import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Robo {

    private static final String URL = "http://xxxx.com.br";
    private static final String URL_REDIRECT = "http://xxxx.com.br";
    private static final String EMAIL = "xxx@xxx.com.br";
    private static final String PASS = "**********";
    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
    private static boolean acceptNextAlert = true;
    private static WebDriver driver;


    public static void main(String[] args) throws InterruptedException {

        List<String> utilsDays = getDates("25/01/2019", null);

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        driver.get(URL);

        // login
        driver.findElement(By.id("UserName")).click();
        driver.findElement(By.id("UserName")).sendKeys(EMAIL);
        driver.findElement(By.id("Pass")).sendKeys(PASS);
        driver.findElement(By.id("btAvancar")).click();

        Thread.sleep(6000);

        String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,"t");
        driver.findElement(By.tagName("body")).sendKeys(selectLinkOpeninNewTab);
        driver.get(URL_REDIRECT);

        for (String day : utilsDays) {
            // novo apontamento
            driver.findElement(By.id("ctl00_Conteudo_novoApontamento")).click();

            Thread.sleep(8000);
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_ddServico")).click();
            new Select(driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_ddServico"))).selectByVisibleText("TMS 180010 - TMS 18-0010 Tokio Marine_...");

            Thread.sleep(8000);
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtData")).click();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtData")).clear();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtData")).sendKeys(day);
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtEntradaAM")).click();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtEntradaAM")).clear();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtEntradaAM")).sendKeys("09:00");
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtSaidaAM")).clear();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtSaidaAM")).sendKeys("12:00");
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtEntradaPM")).clear();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtEntradaPM")).sendKeys("13:00");
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtSaidaPM")).clear();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtSaidaPM")).sendKeys("18:00");
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtDescricao")).clear();
            driver.findElement(By.id("ctl00_Conteudo_apontamentoAtividade_rptDadosApontamento_ctl00_txtDescricao")).sendKeys("Ananlise e Desenvolvimento de Sistemas");

            acceptNextAlert = true;
            driver.findElement(By.id("ctl00_Conteudo_btnSalvar")).click();

            Thread.sleep(5000);
            if(closeAlertAndGetItsText().matches("^Deseja salvar o apontamento da\\(s\\) atividade\\(s\\)[\\s\\S]$")){
                Thread.sleep(5000);
                if("Cadastro efetuado com sucesso!".equals(closeAlertAndGetItsText())) {
                    System.out.println("Apontamento do dia "+day+" realizado com sucesso!!!!");
                }
            }

            Thread.sleep(5000);
        }
    }

    private static String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    public static String getValidDate(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        while (day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY)) {
            System.out.println("Dia "+date.getDayOfMonth() + " Não é uma data válida: ");
            date = date.plusDays(1);
            day = date.getDayOfWeek();
        }
        return date.format(formatter);
    }

    public static String getDate(String stringDate) {
        LocalDate date = LocalDate.parse(stringDate,formatter);
        return getValidDate(date);
    }

    public static String dateToString (LocalDate date) {
        return date.format(formatter);
    }

    public static LocalDate stringToLocalDate (String date) {
        return LocalDate.parse(date,formatter);
    }

    public static List<LocalDate> getListRangeDates(String initialDate, String finalDate) {

        LocalDate start = stringToLocalDate(initialDate);
        LocalDate end = LocalDate.now();

        if (finalDate != null && !finalDate.isEmpty()) {
            LocalDate date = stringToLocalDate(finalDate);
        }

        final int days = (int) start.until(end, ChronoUnit.DAYS);

         return Stream.iterate(start, d -> d.plusDays(1))
                .limit(days)
                .collect(Collectors.toList());
    }

    public static List<String> getStringDatesValid (List<LocalDate> dates) {
        List<String> validDates = new ArrayList<>();

        for(LocalDate date: dates) {
            DayOfWeek day = date.getDayOfWeek();
            if (!day.equals(DayOfWeek.SATURDAY) && !day.equals(DayOfWeek.SUNDAY)) {
                validDates.add(dateToString(date));
            }
        }
        return validDates;
    }

    public static List<String> getDates(String initialDate, String finalDate) {
        List<LocalDate> dts = getListRangeDates(initialDate,finalDate);
        return getStringDatesValid(dts);
    }
}
