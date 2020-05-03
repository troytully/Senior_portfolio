#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

/* Assign a unique ID to this sensor at the same time */

// Buffer to store incoming commands from serial port
String inData;
unsigned long CurrentTime;
unsigned long ElapsedTime;
unsigned long StartTime;

void setup(void) {



#ifndef ESP8266
  while (!Serial)
    ; // will pause Zero, Leonardo, etc until serial console opens
#endif
  // Setup pin 13
  pinMode(13, OUTPUT);
  // Setup pin 12
  pinMode(12, OUTPUT);
  // Set up Reading From Wifi
  Serial.begin(9600);
}



void loop(void) {
  //Serial.println("WAITING");
  while (Serial.available() > 0)
  {
    char recieved = Serial.read();
    inData += recieved;

    // Process message when new line character is recieved
    if (recieved == '\n')
    {
//      Serial.print("Arduino Received: ");
//      Serial.print(inData);

      if (inData == "Alarm\n") { // DON'T forget to add "\n" at the end of the string.
        Serial.println("Turning on LED");
        digitalWrite(13, HIGH); //Turn on LED
        StartTime = millis();

      }

      inData = ""; // Clear recieved buffer
    }
  }
  
  CurrentTime = millis();
  ElapsedTime = CurrentTime - StartTime;
  
  if (ElapsedTime  >= 60 * 200) {
    digitalWrite(13, LOW);//Turn off LED
  }
}
