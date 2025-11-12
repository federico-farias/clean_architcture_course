# Calculator Library

Una librería Java simple que proporciona operaciones matemáticas básicas y utilidades matemáticas avanzadas.

## Características

- **Calculator**: Operaciones matemáticas básicas (suma, resta, multiplicación, división, potencia, raíz cuadrada, valor absoluto)
- **MathUtils**: Utilidades matemáticas avanzadas (factorial, números primos, MCD, MCM)

## Requisitos

- Java 17 o superior
- Maven 3.6+ para compilación

## Compilación

Para compilar la librería:

```bash
mvn compile
```

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

Para generar el JAR de la librería:

```bash
mvn package
```

Esto generará los siguientes archivos en el directorio `target/`:
- `calculator-library-1.0.0.jar` - JAR principal de la librería
- `calculator-library-1.0.0-sources.jar` - JAR con el código fuente
- `calculator-library-1.0.0-javadoc.jar` - JAR con la documentación

## Uso

### Ejemplo básico con Calculator

```java
import com.example.calculator.Calculator;

public class EjemploUso {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        
        double suma = calc.add(5.0, 3.0);        // 8.0
        double resta = calc.subtract(10.0, 4.0); // 6.0
        double mult = calc.multiply(3.0, 7.0);   // 21.0
        double div = calc.divide(15.0, 3.0);     // 5.0
        double pot = calc.power(2.0, 3.0);       // 8.0
        double raiz = calc.sqrt(25.0);           // 5.0
        double abs = calc.abs(-7.5);             // 7.5
        
        System.out.println("Suma: " + suma);
        System.out.println("División: " + div);
    }
}
```

### Ejemplo con MathUtils

```java
import com.example.calculator.MathUtils;

public class EjemploMathUtils {
    public static void main(String[] args) {
        long factorial = MathUtils.factorial(5);    // 120
        boolean esPrimo = MathUtils.isPrime(17);    // true
        int mcd = MathUtils.gcd(48, 18);            // 6
        int mcm = MathUtils.lcm(12, 8);             // 24
        
        System.out.println("Factorial de 5: " + factorial);
        System.out.println("¿Es 17 primo?: " + esPrimo);
        System.out.println("MCD de 48 y 18: " + mcd);
        System.out.println("MCM de 12 y 8: " + mcm);
    }
}
```

## Instalación en otro proyecto

1. Después de compilar con `mvn package`, copia el JAR generado a tu proyecto
2. Agrega el JAR al classpath de tu aplicación
3. O instálalo en tu repositorio local de Maven:

```bash
mvn install
```

Luego en tu proyecto, agrega la dependencia:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>calculator-library</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Estructura del proyecto

```
src/
├── main/java/com/example/calculator/
│   ├── Calculator.java     # Operaciones matemáticas básicas
│   └── MathUtils.java      # Utilidades matemáticas avanzadas
└── test/java/com/example/calculator/
    ├── CalculatorTest.java    # Pruebas para Calculator
    └── MathUtilsTest.java     # Pruebas para MathUtils
```

## Versión

1.0.0 - Versión inicial con operaciones básicas y utilidades matemáticas