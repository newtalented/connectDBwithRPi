/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import DAO.DatasensorDAO;
import bean.Datasensors;
import com.pi4j.wiringpi.Gpio;
import data.DHT11;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huy-lap
 */
public class Test {
    public static void main(String[] args) {
        Random rand= new Random();
        DHT11 dht11= new DHT11();
        
        float temperature=0;
        float humidity=0;
        int ligh= rand.nextInt(100);
       
        Datasensors tblDatasensors= new Datasensors();
        
        for (int i=0; i<1000; i++) {
            for (int j = 0; j < 15; j++) {
                dht11.getHumidityAndTemperature();
                temperature= dht11.getTemperature();
                humidity= dht11.getHumidity();
                ligh= rand.nextInt(100);
                if(temperature > 0)
                {
                    System.out.println("Hehe- temperature = " + temperature);
                    System.out.println("Hehe- humidity = " + humidity);
                    tblDatasensors.setTemp((int)temperature);
                    tblDatasensors.setHumidity((int)humidity);
                    tblDatasensors.setLight(ligh);
                    
                    DAO.DatasensorDAO controllDatasensor= new DatasensorDAO();
                    if(controllDatasensor.insertIntoTable(tblDatasensors)){
                        System.out.println("Đã nhập Dữ liệu vào database");
                    }
                    else{
                        System.out.println("Lỗi");
                    }
                    break;
                }
                if(j==14){
                    System.out.println("Data not good");
                }

            }
//            Thread.sleep(10000);
            Gpio.delay(5000);
        }
    }
}
