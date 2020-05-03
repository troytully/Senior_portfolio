#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

/* Assign a unique ID to this sensor at the same time */
Adafruit_LSM303_Accel_Unified accel = Adafruit_LSM303_Accel_Unified(54321);

// Buffer to store incoming commands from serial port
String inData;
//For keep tracking of times
unsigned long CurrentTime;
unsigned long ElapsedTime;
unsigned long StartTime;

void displaySensorDetails(void) {
  sensor_t sensor;
  accel.getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.print("Sensor:       ");
  Serial.println(sensor.name);
  Serial.print("Driver Ver:   ");
  Serial.println(sensor.version);
  Serial.print("Unique ID:    ");
  Serial.println(sensor.sensor_id);
  Serial.print("Max Value:    ");
  Serial.print(sensor.max_value);
  Serial.println(" m/s^2");
  Serial.print("Min Value:    ");
  Serial.print(sensor.min_value);
  Serial.println(" m/s^2");
  Serial.print("Resolution:   ");
  Serial.print(sensor.resolution);
  Serial.println(" m/s^2");
  Serial.println("------------------------------------");
  Serial.println("");
  delay(500);
}

void setup(void) {
  
#ifndef ESP8266
  while (!Serial)
    ; // will pause until serial console opens
#endif
  Serial.begin(9600);
  Serial.println("Accelerometer Test");
  Serial.println("");

  // Setup pin 13 Alarm
  pinMode(13, OUTPUT);
    // Setup pin 12 LED
  pinMode(12, OUTPUT);
  // Set up Reading From Wifi
  Serial.begin(9600);

  /* Initialise the sensor */
  if (!accel.begin()) {
    /* There was a problem detecting the ADXL345 ... check your connections */
    Serial.println("Ooops, no LSM303 detected ... Check your wiring!");
    while (1)
      ;
  }

  /* Display some basic information on this sensor */
  displaySensorDetails();

  accel.setRange(LSM303_RANGE_4G); //Set what range you would like
  Serial.print("Range set to: ");
  lsm303_accel_range_t new_range = accel.getRange();
  switch (new_range) {
    case LSM303_RANGE_2G:
      Serial.println("+- 2G");
      break;
    case LSM303_RANGE_4G:
      Serial.println("+- 4G");
      break;
    case LSM303_RANGE_8G:
      Serial.println("+- 8G");
      break;
    case LSM303_RANGE_16G:
      Serial.println("+- 16G");
      break;
  }

  accel.setMode(LSM303_MODE_NORMAL);
  Serial.print("Mode set to: ");
  lsm303_accel_mode_t new_mode = accel.getMode();
  switch (new_mode) {
    case LSM303_MODE_NORMAL:
      Serial.println("Normal");
      break;
    case LSM303_MODE_LOW_POWER:
      Serial.println("Low Power");
      break;
    case LSM303_MODE_HIGH_RESOLUTION:
      Serial.println("High Resolution");
      break;
  }
}



void loop(void) {
  /* Get a new sensor event */
  sensors_event_t event;
  accel.getEvent(&event);
  int vibration = pow(event.acceleration.x, 2) + pow(event.acceleration.y, 2) + pow(event.acceleration.z, 2); //Random algorithm

  Serial.println(vibration);

  if (vibration > 115) { //Value I personally decided would be good although never tested out on trails
    // sound the alarm
    digitalWrite(13, HIGH);
    Serial.print("Alarm\n");
      /* Delay before the next sample */
    delay(55);
    StartTime = millis(); //Start timer
  }
  while (Serial.available() > 0) //Run until there is no serial not good to have running all the time
  {
    char recieved = Serial.read(); //Take next char
    inData += recieved;             //Concat with previous chars

    // Process message when new line character is recieved
    if (recieved == '\n')
    {

      if (inData == "Alarm\n") { // DON'T forget to add "\n" at the end of the string.
        digitalWrite(12, HIGH); //Turn on Alarm
         StartTime = millis(); //Start timer
      }

      inData = ""; // Clear recieved buffer
    }
  }

    digitalWrite(13, LOW);//Turn off Alarm
    
  CurrentTime = millis();
  ElapsedTime = CurrentTime - StartTime;
  
  if(ElapsedTime  >=  60000*2 ){
    digitalWrite(12, LOW);//Turn off LED
  }
  
//  /* Display the results (acceleration is measured in m/s^2) */
//  Serial.print("X: ");
//  Serial.print(event.acceleration.x);
//  Serial.print("  ");
//  Serial.print("Y: ");
//  Serial.print(event.acceleration.y);
//  Serial.print("  ");
//  Serial.print("Z: ");
//  Serial.print(event.acceleration.z);
//  Serial.print("  ");
//  Serial.println("m/s^2");

  /* Delay before the next sample */
  delay(33);
}
