package com.mycompany.app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;

public class AppTest {
    private String name;//тут вот явный костыль(( нужно поискать более красивое решение

    //Для простоты конструкции тестов исполуется фреймворк Selenide https://selenide.org
    //При ошибках создается скриншот в папке /build/reports/test
    //После завершения теста создается слепок html + screenshoot в папке build/reports/test
    //запуск возможен из командной строки, в данном случае через maven
    //mvn test
    //либо из IDE, в данном случае использована JB IDEA
    //Before в данном случае используется как контейнер, который определяет тип браузера, чистит кеш,
    //запускается и открывает тестируемую страницу
    //Test описывает тест-кейс, кейсы выполняются последовательно, после выполнения кейса окно браузера закрывается
    @Before
    //Указать папку с webdriver, Определить браузер, открыть страницу
    public void start() {
        //у меня локально установлен chromewebdriver, так что строку можно закомментировать, selenide подтянет все из пакета.
        //System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        System.setProperty("selenide.browser", "chrome");
        clearBrowserCache();
        open("https://dev-shop.jowi.club/auth/sign-in");
    }
    @After
    public void end() {
        char[] hash = new char[4]; //набор символов для имени файла, чтобы исключить повторения (стоит найти более красивое решение)
        screenshot("/" + name + "_finish_screenshoot" + hash.toString());
        close();
    }
    @Test
    //Загрузка страницы авторизации, подождать пока загрузится форма авторизации, ввести не валидные данные
    public void test00() {
        //присваиваем имя теста
        name = "test00";
        //ищем элемент input name="login" на странице авторизации после того как форма загрузилась
        //прописываем в поле значение test
        $(By.name("login")).shouldBe(visible).setValue("test");
        //input принимает только цифры, значит должно быть отображено сообщение об ошибке
        $(".alert").should(have(text("Введите телефон, указанный при регистрации")));
        //проверка: элемент input name="password" должен быть отображен на странице
        $(By.name("password")).shouldBe(visible);
    }
    @Test
    //Загрузка страницы авторизации, подождать пока загрузится форма авторизации, ввести не валидный пароль
    public void test01() {
        name = "test01";
        $(By.name("login")).shouldBe(visible).setValue("+79653836312");
        $(".alert").shouldHave(text("Введите телефон, указанный при регистрации"));
        $(By.name("password")).shouldBe(visible).setValue("0987654321");
        $(".btn-primary").click();
        $(".title").shouldHave(text("Ошибка"));
        $(".text").shouldHave(text("[20] Неправильный Login или пароль.."));

    }
    @Test
    //Загрузка страницы авторизации, подождать пока загрузится форма авторизации, ввести валидные данные
    //Загрузка страницы компании
    public void test02() {
        name = "test02";
        $(By.name("login")).shouldBe(visible).setValue("+79653836312");
        $(By.name("password")).shouldBe(visible).setValue("1234567890");
        $(".btn-primary").click();
        $(".sidebar").shouldBe(visible);
        $(".card").shouldBe(visible);
    }
}
