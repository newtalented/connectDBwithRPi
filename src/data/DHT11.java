/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

/**
 *
 * @author huy-lap
 */
public class DHT11 {
    
    private static final int MAXTIMINGS = 85;
    private int[] dht11_dat = { 0, 0, 0, 0, 0 };
    private float temperature=0;
    private float humidity=0;
    
    public DHT11() {
        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

       GpioUtil.export(3, GpioUtil.DIRECTION_OUT);            
    }

    public float getTemperature() {
        return temperature;
    }
    
    public float getHumidity() {
        return humidity;
    }

    public void getHumidityAndTemperature() {
       int laststate = Gpio.HIGH;
       int j = 0;
       float c = 0 ;
       float h = 0 ;
       dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;

       Gpio.pinMode(3, Gpio.OUTPUT);
       Gpio.digitalWrite(3, Gpio.LOW);
       Gpio.delay(18);

       Gpio.digitalWrite(3, Gpio.HIGH);        
       Gpio.pinMode(3, Gpio.INPUT);

       for (int i = 0; i < MAXTIMINGS; i++) {
          int counter = 0;
          while (Gpio.digitalRead(3) == laststate) {
              counter++;
              Gpio.delayMicroseconds(1);
              if (counter == 255) {
                  break;
              }
          }

          laststate = Gpio.digitalRead(3);

          if (counter == 255) {
              break;
          }

          /* ignore first 3 transitions */
          if ((i >= 4) && (i % 2 == 0)) {
             /* shove each bit into the storage bytes */
             dht11_dat[j / 8] <<= 1;
             if (counter > 16) {
                 dht11_dat[j / 8] |= 1;
             }
             j++;
           }
        }
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
        if ((j >= 40) && checkParity()) {
            h = (float)((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if ( h > 100 )
            {
                h = dht11_dat[0];   // for DHT11
            }
            c = (float)(((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if ( c > 125 )
            {
                c = dht11_dat[2];   // for DHT11
            }
            if ( (dht11_dat[2] & 0x80) != 0 )
            {
                c = -c;
            }
            
            float f = c * 1.8f + 32;
            this.temperature= c;
            this.humidity=h;
            System.out.println( "Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
        }
        else  {
//            System.out.println( "Data not good, skip" );
            this.temperature=0;
        }
    }

    private boolean checkParity() {
      return (dht11_dat[4] == ((dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3]) & 0xFF));
    }



    public static void getData() throws Exception {

        DHT11 dht = new DHT11();

        for (int i=0; i<1000; i++) {
            for (int j = 0; j < 15; j++) {
                dht.getHumidityAndTemperature();
                float temp= dht.getTemperature();
                float humi= dht.getHumidity();
                if(temp > 0)
                {
                    System.out.println("Hehe- temperature = " + temp);
                    System.out.println("Hehe- humidity = " + humi);
                    break;
                }
                if(j==14){
                    System.out.println("Data not good");
                }

            }
//            Thread.sleep(10000);
            Gpio.delay(5000);
        }
        System.out.println("Done!!");
    }
}
