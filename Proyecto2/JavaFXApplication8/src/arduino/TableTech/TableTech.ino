#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);  // Inicializar el objeto LCD

int ledPin1 = 5;  // Pin del LED 10
int ledPin2 = 11; // Pin del LED 11
int ledPin3 = 12; // Pin del LED 12

void setup() {
  Serial.begin(9600);  // Iniciar la comunicación serie a 9600 baudios
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);

  lcd.begin(16, 2);  // Inicializar el LCD de 16x2
  lcd.print("Cola:");  // Imprimir etiqueta "Señal:" en la primera línea del LCD
}

void loop() {
  if (Serial.available() > 0) {
    int receivedSignal = Serial.parseInt();  // Leer el número enviado desde Java
    if (receivedSignal < 1000) {
      lcd.clear();
      lcd.begin(16, 2);  // Inicializar el LCD de 16x2
      lcd.print("Cola:");  // Imprimir etiqueta "Señal:" en la primera línea del LCD
      lcd.setCursor(7, 1);
      lcd.print(receivedSignal);
    }

    // Encender el LED correspondiente según la señal recibida
    if (receivedSignal == 2000) {
      digitalWrite(ledPin1, HIGH);
      digitalWrite(ledPin2, LOW);
      digitalWrite(ledPin3, LOW);

    } else if (receivedSignal == 3000) {
      digitalWrite(ledPin1, LOW);
      digitalWrite(ledPin2, HIGH);
      digitalWrite(ledPin3, LOW);

    } else if (receivedSignal == 4000) {
      digitalWrite(ledPin1, LOW);
      digitalWrite(ledPin2, LOW);
      digitalWrite(ledPin3, HIGH);

    }
  }
}
