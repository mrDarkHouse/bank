package ru.vsu.cs.postnikov.banktask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vsu.cs.postnikov.banktask.UI.UIConnector;

public class App {

    public static void main(String[] args){
        new App().run();
    }

    public void run(){
        ApplicationContext context = new AnnotationConfigApplicationContext(SwingConfig.class);
        UIConnector connector = context.getBean(UIConnector.class);
        connector.show();
    }
}
